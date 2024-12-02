# -*- coding: utf-8 -*-
import requests
from bs4 import BeautifulSoup
import mysql.connector
import pandas as pd
import re

def get_disaster_news(url):
    response = requests.get(url)
    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        news_data = []

        for item in soup.find_all('h3', class_='tit-news'):
            a_tag = item.find('a')
            if a_tag:
                title = a_tag.get_text(strip=True)
                link = a_tag['href']
                if not link.startswith('http'):
                    link = 'https://www.yna.co.kr' + link

                date_span = item.find_next_sibling('span', class_='tt')
                date_time = date_span.get_text(strip=True) if date_span else 'No date available'

                parent = item.parent
                p_lead = parent.find('p', class_='lead') if parent else None
                content = p_lead.get_text(strip=True) if p_lead else 'No content available'

                news_data.append({
                    'date': date_time,
                    'title': title,
                    'link': link,
                    'content': content
                })

        news_df = pd.DataFrame(news_data, columns=['date', 'title', 'link', 'content'])
        news_df['identifier'] = news_df['link'].apply(extract_identifier)
        news_df.drop_duplicates(subset=['identifier'], keep='first', inplace=True)

        return news_df
    else:
        print(f"데이터 검색 실패: {response.status_code}")
        return pd.DataFrame(columns=['date', 'title', 'link', 'content', 'identifier'])

def extract_identifier(link):
    match = re.search(r'/view/(\w+)', link)
    return match.group(1) if match else None

def insert_news(connection, news_df):
    cursor = connection.cursor()
    sql = """
    INSERT INTO disaster_news (date_time, title, link, content, identifier)
    VALUES (%s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE
    date_time = VALUES(date_time),
    content = VALUES(content)
    """
    inserted_count = 0
    for _, row in news_df.iterrows():
        cursor.execute(sql, (row['date'], row['title'], row['link'], row['content'], row['identifier']))
        inserted_count += cursor.rowcount
    connection.commit()
    cursor.close()
    return inserted_count

def create_table(connection):
    cursor = connection.cursor()
    sql = """
    CREATE TABLE IF NOT EXISTS disaster_news (
        id INT AUTO_INCREMENT PRIMARY KEY,
        date_time VARCHAR(50),
        title VARCHAR(255) NOT NULL,
        link VARCHAR(255) NOT NULL,
        content TEXT,
        identifier VARCHAR(255) UNIQUE NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_link (link),
        UNIQUE KEY unique_title (title)
    )  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    """
    cursor.execute(sql)
    connection.commit()
    cursor.close()

def check_table_and_data(connection):
    cursor = connection.cursor(dictionary=True)
    cursor.execute("SELECT COUNT(*) FROM disaster_news")
    count = cursor.fetchone()['COUNT(*)']
    print(f"테이블의 총 레코드 수: {count}")

    cursor.execute("SELECT * FROM disaster_news ORDER BY id DESC LIMIT 5")
    print("최근 5개 레코드:")
    for row in cursor.fetchall():
        print(row)
    cursor.close()

def main():
    print("뉴스 크롤러 실행 중...")
    url = "https://www.yna.co.kr/safe/news"
    news_df = get_disaster_news(url)

    connection = None
    try:
        # 데이터베이스 연결 정보를 직접 입력합니다.
        connection = mysql.connector.connect(
            host='210.119.33.4',  # 데이터베이스 호스트
            port=3306,         # 포트 번호
            user='dohwan',       # 사용자 이름
            password='1234',  # 비밀번호
            database='BASIC',  # 데이터베이스 이름
            charset='utf8mb4'
        )

        create_table(connection)
        inserted_count = insert_news(connection, news_df)
        print(f"데이터 처리 완료. MySQL 데이터베이스에 {inserted_count}개의 새로운 레코드가 삽입되었습니다.")
        check_table_and_data(connection)

    except mysql.connector.Error as err:
        print(f"데이터베이스 오류: {err}")
    except Exception as e:
        print(f"예상치 못한 오류: {e}")
    finally:
        if connection and connection.is_connected():
            connection.close()

if __name__ == '__main__':
    main()