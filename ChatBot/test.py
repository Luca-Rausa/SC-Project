# Check if the server is running on port 5000
import requests

if __name__ == "__main__":
    url = "http://127.0.0.1:5000"

    try:
        response = requests.get(url)
        print("Server is running")
    except requests.exceptions.ConnectionError:
        print("Server is not running")
