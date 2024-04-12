import requests
from bs4 import BeautifulSoup

if __name__ == "__main__":
    url = "https://stegercenter.vt.edu/faculty/faq.html"
    response = requests.get(url)
    soup = BeautifulSoup(response.text, "html.parser")

    # Example: Extracting all text from paragraphs
    paragraphs = soup.find_all("p")
    for paragraph in paragraphs:
        print(paragraph.text)