from flask import Flask, request, jsonify
from transformers import AutoModelForCausalLM, AutoTokenizer
import torch
from fuzzywuzzy import fuzz
import time

app = Flask(__name__)

class Chatbot:
    def __init__(self):
        self.model_name = "microsoft/DialoGPT-medium"
        self.tokenizer = AutoTokenizer.from_pretrained(self.model_name,padding_side="left")
        self.model = AutoModelForCausalLM.from_pretrained(self.model_name)
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)
        self.chat_history_ids = None
        self.intents = [
            {
                "patterns": ["mission of Steger Center","mission of SC"],
                "responses": ["The mission of Outreach and International Affairs at Virginia Tech is to share the best of Virginia Tech by working alongside communities across the world."]
            },
            {
                "patterns": ["study abroad"],
                "responses": ["Virginia Tech aims to help students engage locally in Riva San Vitale, Switzerland, to make the most of their study abroad experience."]
            },
            {
                "patterns": ["project-based learning", "directed research projects", "engagement with local schools or sports clubs"],
                "responses": ["Virginia Tech offers opportunities for project-based learning, directed research projects, and engagement with local schools or sports clubs to help students feel at home in Riva San Vitale."]
            },
            {
                "patterns": ["location of Steger Center", "where is Steger Center"],
                "responses": ["The Steger Center for International Scholarship is located in Riva San Vitale, Ticino, Switzerland."]
            },
            {
                "patterns": ["location of Riva", "where is Riva"],
                "responses": [
                    "Riva San Vitale is a municipality in the canton of Ticino in Switzerland, located in the district of Mendrisio"]
            },
            {
                "patterns": ["accommodations", "where to stay", "hotels in the area", "lodging options"],
                "responses": ["There are various accommodation options available in the area, including hotels, bed and breakfasts, and guest houses. Some popular choices include 1)Hotel Svizzero  ,2)Cherry B&B  ,3)Grand Hotel Villa Castagnola  ,4)Hotel Admiral Lugano  ,5)Hotel Colorado, 6)Hotel de la Paix, 7)Hotel Delfino, 8)Hotel Federale, 9)Hotel International Au Lac, 10)Hotel Lugano Dante Center, 11)IBIS Hotel Lugano Paradiso, 12)Novotel Lugano Paradiso, 13)Splendide Royal Lugano, 14)Swiss Diamond Hotel, 15)Suitenhotel Parco Paradiso, 16)The View Lugano, 17)Villa Principe Leopoldo, 18)Villa Sassa Hotel & Residence Wellness & SPA, 19)Guest House Lugano Center, 20)AirB&B also has numerous options in the area."]
            },

            {
                "patterns": ["facilities", "what does Steger Center offer"],
                "responses": ["The Steger Center offers various facilities and amenities such as classrooms, study spaces, accommodations, dining options, and recreational areas."]
            },
            {
                "patterns": ["programs", "courses", "study options", "what programs are available at Steger Center"],
                "responses": ["The Steger Center offers a wide range of study programs and courses, including short-term study away programs, semester abroad programs, and specialized courses in various fields."]
            },
            {
                "patterns": ["application process", "how to apply", "how do I apply to Steger Center"],
                "responses": ["You can find information about the application process on the Steger Center's website or contact the center directly for assistance with the application."]
            },
            {
                "patterns": ["activities in steger center", "events", "what activities are organized at Steger Center"],
                "responses": ["The Steger Center organizes various activities and events for students and faculty, including cultural excursions, workshops, guest lectures, and community engagement projects and activities outside of class."]
            },
            {
                "patterns": ["programs outside of class", "activities outside of class", "what programs does the Steger Center offer outside of class time"],
                "responses": ["The Steger Center offers various programs for students and faculty outside of class time, designed to help them explore their surroundings and enhance their experience abroad."]
            },
            {
                "patterns": ["navigate surroundings", "help navigating new surroundings", "how does the Steger Center help students navigate their new surroundings"],
                "responses": ["The Steger Center provides assistance and resources to help students navigate their new surroundings, including orientation sessions, local maps, and recommendations for activities and destinations."]
            },
            {
                "patterns": ["day trips", "local events", "sites to visit", "what day trips are offered by the Steger Center", "what local sites are recommended"],
                "responses": ["The Steger Center organizes day trips to various destinations in Ticino and Switzerland, allowing students to explore local attractions, events, and cultural sites."]
            },
            {
                "patterns": ["recommended towns", "towns to visit", "places to visit in Ticino", "what towns are recommended to visit in Ticino, Switzerland"],
                "responses": ["Recommended towns to visit in Ticino include Lugano, Bellinzona, and Locarno, each offering unique experiences, historical sites, and scenic views."]
            },
            {
                "patterns": ["long weekend trips", "nearby destinations", "destinations for long weekends", "where can students travel on long weekends"],
                "responses": ["Being located in the center of Europe, the Steger Center offers easy access to nearby destinations for long weekend trips, allowing students to explore Switzerland and beyond."]
            },
            {
                "patterns": ["exploring surroundings", "experiencing Switzerland", "activities to experience Switzerland"],
                "responses": ["Exploring Switzerland's surroundings and culture can include activities such as hiking, visiting local markets, sampling Swiss cuisine, and participating in cultural events."]
            },
            {
                "patterns": ["landscapes", "are there guided walks available at the Steger Center"],
                "responses": ["The Steger Center offers guided walks and hikes starting from its doorstep, providing students with opportunities to explore the surrounding landscapes, nature trails, and scenic viewpoints."]
            },
            {
                "patterns": ["essential items", "what to bring", "packing essentials" ,"bring with me"],
                "responses": ["When exploring the surroundings, it's essential to bring items such as comfortable walking shoes, weather-appropriate clothing, water bottles, sunscreen, and a camera to capture memorable moments."]
            },
            {
                "patterns": ["location of Riva San Vitale", "where is Riva San Vitale located"],
                "responses": ["Riva San Vitale is a small town of about 2,600 people located in the Canton of Ticino, in the southern part of Switzerland, near Lugano."]
            },
            {
                "patterns": ["notable features of Riva San Vitale", "features of Riva San Vitale"],
                "responses": ["Riva San Vitale is situated between two mountain peaks, Monte Generoso and Monte San Giorgio, and borders Lake Lugano."]
            },
            {
                "patterns": ["recreational activities in Riva San Vitale", "activities in Riva San Vitale", "what recreational activities are available"],
                "responses": ["Recreational activities in Riva San Vitale include biking, playing basketball or soccer, and enjoying the garden and patio at the Steger Center."]
            },
            {
                "patterns": ["amenities at the Steger Center", "what amenities are provided"],
                "responses": ["Amenities at the Steger Center include an espresso machine, bikes, guitars, yoga mats, basketballs, and soccer balls for recreational use."]
            },
            {
                "patterns": ["dining options in Riva San Vitale", "where to eat in Riva San Vitale","eat"],
                "responses": ["Dining options in Riva San Vitale include various restaurants, gelato stores, and the local supermarket, all within walking distance from the Steger Center."]
            },
            {
                "patterns": ["travel from Riva San Vitale to Lugano", "transportation to Lugano"],
                "responses": ["Students can travel from Riva San Vitale to Lugano by taking the 20-minute train ride from the Capolago-Riva San Vitale train station."]
            },
            {
                "patterns": ["transportation options in Riva San Vitale", "how to get around in Riva San Vitale"],
                "responses": ["Transportation options in Riva San Vitale include the Capolago-Riva San Vitale train station, buses, and bike paths connecting to nearby towns."]
            },
            {
                "patterns": ["engagement with the local community", "community engagement in Riva San Vitale"],
                "responses": ["Students can engage with the local community in Riva San Vitale by using the facilities of the local middle school for sports activities."]
            },
            {
                "patterns": ["landmarks in Riva San Vitale", "landmarks in Riva San Vitale"],
                "responses": ["Riva San Vitale features historical landmarks such as the Riva San Vitale Baptistery and the Ferrovia Monte Generoso cog railway."]
            },
            {
                "patterns": ["attractions in Lugano", "to do in Lugano", "places to visit in Lugano"],
                "responses": ["In Lugano, students can explore attractions such as shopping districts, recreational areas, cultural sites, and scenic viewpoints."]
            },
            {
                "patterns": ["airports"],
                "responses": ["The nearest airports to Riva San Vitale are Zurich Airport (Flughafen Zürich - ZRH) and Milan Malpensa Airport (MXP)."]
            },
            {
                "patterns": ["travel from Zurich Airport", "transportation from Zurich Airport","zurich airport"],
                "responses": ["The easiest way to travel from Zurich Airport to Lugano/Riva is by train. Follow signs to the train station at the airport and check the online timetable for trains going to Riva San Vitale. The travel time is approximately 3 hours."]
            },
            {
                "patterns": ["travel from Milan Airport", "transportation from Milan Malpensa Airport","milan airport"],
                "responses": ["The easiest way to travel from Milan Malpensa Airport to Lugano and Riva San Vitale is by train. Follow signs to the train station at the airport and check the online timetable for trains going to Riva San Vitale. The travel time is approximately 1 hour and 30 minutes."]
            },
            {
                "patterns": ["train station location in Riva San Vitale", "where is the train station in Riva San Vitale"],
                "responses": ["The train station in Riva San Vitale is located 15 minutes (on foot) from the Steger Center."]
            },
            {
                "patterns": ["Switzerland's cultural diversity", "Swiss culture"],
                "responses": ["Switzerland is known for its cultural diversity, with distinct differences in languages, traditions, and customs across regions."]
            },
            {
                "patterns": ["Languages spoken in Switzerland","languages"],
                "responses": ["Switzerland has four main languages: German, French, Italian, and Romansh, spoken in different regions of the country."]
            },
            {
                "patterns": ["Distinct cultural differences in Swiss regions", "Swiss regional cultures"],
                "responses": ["Traveling through Switzerland, you'll notice distinct cultural differences in various regions, shaped by language, history, and traditions."]
            },
            {
                "patterns": ["language in riva San Vitale", "Italian-speaking region of Switzerland"],
                "responses": ["Riva San Vitale is located in the Italian-speaking region of Switzerland, offering a unique cultural experience."]
            },
            {
                "patterns": ["Swiss characteristics"],
                "responses": ["Swiss culture values cleanliness, efficiency, and safety, contributing to its reputation as a well-organized and secure country."]
            },
            {
                "patterns": ["Social norms and behavior in Switzerland", "Swiss social customs"],
                "responses": ["Swiss society follows unwritten social rules regarding behavior in daily life, which may take time for newcomers to adjust to."]
            },
            {
                "patterns": ["Language use in Switzerland", "Swiss language etiquette"],
                "responses": ["Swiss appreciate efforts to speak their native language, although many also speak English. Learning key phrases in German, French, or Italian can enhance your experience."]
            },
            {
                "patterns": ["Appreciation for attempting to speak the native language", "Speaking Swiss languages"],
                "responses": ["Attempting to speak the native language shows respect and is appreciated by Swiss locals, even if you're not fluent."]
            },
            {
                "patterns": ["Learning key phrases in German and French", "Swiss language phrases"],
                "responses": ["Learning key phrases in German and French can help you communicate effectively during your stay in Switzerland."]
            },
            {
                "patterns": ["Greeting in Italian", "Italian greeting"],
                "responses": ["'Buongiorno' is a friendly greeting in Italian, commonly used in Riva San Vitale and other Italian-speaking regions."]
            },
            {
                "patterns": ["Adjusting to Swiss social customs", "Adapting to Swiss culture"],
                "responses": ["Adjusting to Swiss social customs may take time, but observing and learning from locals can help you adapt more easily."]
            },
            {
                "patterns": ["Exploring Switzerland during study abroad", "Studying abroad in Switzerland"],
                "responses": ["Studying abroad in Switzerland offers the opportunity to explore its diverse culture, landscapes, and traditions."]
            },
            {
                "patterns": ["Experiencing Swiss diversity firsthand", "Swiss cultural diversity"],
                "responses": ["Experience Swiss diversity firsthand by immersing yourself in different regions, languages, and cultural practices."]
            },
            {
                "patterns": ["Observing Swiss social behavior", "Social behavior in Switzerland"],
                "responses": ["Observing Swiss social behavior can help you understand local customs and norms, facilitating smoother interactions."]
            },
            {
                "patterns": ["Getting accustomed to Swiss culture", "Adapting to Swiss lifestyle"],
                "responses": ["Getting accustomed to Swiss culture involves learning about its traditions, language, and social etiquette."]
            },
            {
                "patterns": ["Steger Center operating hours", "Center hours"],
                "responses": ["The Steger Center is open Monday through Friday from 08:00 to 18:00."]
            },
            {
                "patterns": ["Contact information","email","telephone", "Center contact info"],
                "responses": ["You can contact the Steger Center via email at stegercenter@vt.edu or by telephone at (011) 41 91 648 3651."]
            },
            {
                "patterns": ["address for the Steger Center", "Center address"],
                "responses": ["The mailing address for the Steger Center is: Steger Center for International Scholarship, Villa Maderni, via Settala 8, 6826 Riva San Vitale, Switzerland."]
            },
            {
                "patterns": ["Social media","Steger Center Instagram", "Center Instagram"],
                "responses": ["You can follow the Steger Center on Instagram for updates and news."]
            },
            {
                "patterns": ["Emergency contact information in Switzerland", "emergency contacts"],
                "responses": ["In case of emergency in Switzerland, you can contact the following: General emergency number: 112, Ambulance: 144, Police: 117, Fire: 118."]
            },
            {
                "patterns": ["General emergency number in Switzerland: 112", "Swiss emergency number"],
                "responses": ["The general emergency number in Switzerland is 112."]
            },
            {
                "patterns": ["Ambulance", "Swiss ambulance contact"],
                "responses": ["To reach an ambulance in Switzerland, dial 144."]
            },
            {
                "patterns": ["Police", "Swiss police contact"],
                "responses": ["To contact the police in Switzerland, dial 117."]
            },
            {
                "patterns": ["Fire", "Swiss fire department contact"],
                "responses": ["To reach the fire department in Switzerland, dial 118."]
            },
            {
                "patterns": ["Types of programs at the Steger Center", "Programs ","Details of programs","Steger Center programs"],
                "responses": [
                    "The Steger Center offers both full semester and short-term programs.full semester programs offered during the fall and spring semesters, while short-term programs are available in May and June each year."]
            },
            {
                "patterns": ["Full semester programs at the Steger Center", "Full semester"],
                "responses": [
                    "The Steger Center hosts full semester programs during the fall and spring semesters. Orientation begins on the Thursday prior to the first day of classes in Blacksburg. The first day of classes at the Steger Center coincides with the first day of classes in Blacksburg."]
            },
            {
                "patterns": ["Orientation schedule for full semester programs", "Full semester orientation"],
                "responses": [
                    "Orientation for full semester programs begins on the Thursday prior to the first day of classes in Blacksburg."]
            },
            {
                "patterns": ["Coordinated academic travel and experiential learning", "Academic travel"],
                "responses": [
                    "The Steger Center provides time for coordinated academic travel and experiential learning, with the center closing for two weeks each term, normally during the fifth and 10th weeks of the semester."]
            },
            {
                "patterns": ["Closure dates for full semester programs", "Semester closure dates"],
                "responses": [
                    "Please pay close attention to the dates in the calendar on the Steger Center webpage for the closure dates of full semester programs."]
            },
            {
                "patterns": ["semester dates",
                             "calendar for semester dates"],
                "responses": [
                    "It's important to check the calendar on the Steger Center webpage when planning your semester, as it provides important dates and closure information."]
            },
            {
                "patterns": ["Short-term programs at the Steger Center", "Short-term"],
                "responses": [
                    "The Steger Center can also host short-term programs for undergraduate and graduate programs in May and June each year."]
            },
            {
                "patterns": ["Availability of short-term programs in May and June", "Short-term programs availability"],
                "responses": [
                    "Short-term programs are available at the Steger Center in May and June each year for undergraduate and graduate students."]
            }

        ]

    def chat(self, user_input):
        user_input = user_input.lower()

        # Initialize variables to store the best match and its corresponding intent
        best_match_ratio = 0
        best_intent_response = None

        # Check for intents defined in the intents list
        for intent in self.intents:
            for pattern in intent["patterns"]:
                # Calculate similarity score using fuzzy string matching
                match_ratio = fuzz.partial_ratio(user_input, pattern.lower())
                # If the current match is better than the previous best match
                if match_ratio > best_match_ratio:
                    best_match_ratio = match_ratio
                    best_intent_response = intent["responses"][0]

        # If the best match ratio is above a certain threshold, return the corresponding response
        if best_match_ratio >= 80:
            return best_intent_response

        # If no close match is found, proceed with the dialogue generation
        new_user_input_ids = self.tokenizer.encode(user_input + self.tokenizer.eos_token, return_tensors='pt').to(
            self.device)
        bot_input_ids = torch.cat([self.chat_history_ids, new_user_input_ids],
                                  dim=-1) if self.chat_history_ids is not None else new_user_input_ids

        self.chat_history_ids = self.model.generate(bot_input_ids, max_length=1000,
                                                    pad_token_id=self.tokenizer.eos_token_id, temperature=0.9,
                                                    num_return_sequences=1)
        response = self.tokenizer.decode(self.chat_history_ids[:, bot_input_ids.shape[-1]:][0],
                                         skip_special_tokens=True)
        return response

chatbot = Chatbot()

@app.route('/chat', methods=['POST'])
def chat():
    user_input = request.json['user_input']
    bot_response = chatbot.chat(user_input)
    time.sleep(1)
    return jsonify({"bot_response": bot_response})

if __name__ == "__main__":
    app.run(debug=True)

