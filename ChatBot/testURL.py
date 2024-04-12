import requests

if __name__ == "__main__":
    url = "http://127.0.0.1:5000/chat"
    user_input = {"user_input": "how ar u"}
    response = requests.post(url, json=user_input)
    data = response.json()
    print("Bot's response:", data["bot_response"])