<!-- PROJECT SHIELD -->
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-0095D5?style=for-the-badge&logo=ktor&logoColor=white)


# 🌐 ktor-api-client — Kotlin Ktor REST API with Firebase & MySQL

**ktor-api-client** is a powerful open-source Kotlin project that demonstrates how to build modern, scalable RESTful APIs using the [Ktor](https://ktor.io/) framework.

This API client supports both **Firebase Firestore** and **MySQL** for data storage, and includes **authentication APIs** like login, register, and user listing with image upload support.

## 🚀 Why Use ktor-api-client?

- ✅ Build production-ready **REST APIs in Kotlin**
- 🔥 Integrates with **Firebase Firestore** and **MySQL**
- 🧪 Fully working **Ktor authentication API** example
- 📁 User image uploads stored locally in the `/uploads` directory
- 💻 Perfect for backend developers learning **Ktor + Kotlin + Firestore + MySQL**


## 🔑 Features (REST API Endpoints)

### 🔐 `/login` (POST)
Authenticate user credentials.  
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

### 📝 `/register` (POST)
Register a new user.  
**Request Body** (multipart/form-data):
- `name`: String
- `email`: String
- `password`: String
- `userImage`: File

### 📋 `/users` (GET)
Get a list of all registered users.


## ⚙️ How to Setup and Run

> 📌 Make sure you update your local IP in `Base.kt` before running the project.

### 🔧 Steps:

1. Clone the repository  
   ```bash
   git clone https://github.com/your-username/ktor-api-client.git
   ```

2. Open in Android Studio or IntelliJ IDEA

3. Change IP in `Base.kt` to your local or hosted server IP

4. Run the server  
   ```bash
   ./gradlew run
   ```



## 💡 Use Cases

- 🔐 Learn **Ktor authentication**
- 🔄 REST API integration with **Firebase + MySQL**
- 🧰 Use as boilerplate for Kotlin backend projects
- 🧪 Practice handling file uploads and image storage in Ktor



## 📌 Keywords for Search Optimization

> `ktor`, `kotlin`, `kotlin ktor`, `ktor rest api`, `ktor firebase mysql`, `authentication api`, `kotlin backend`, `ktor client server`, `kotlin rest api`, `ktor image upload`, `kotlin api example`



## 📬 Postman API Collection

Test all APIs easily using the included Postman collection.  
📥 [Download Postman Collection](https://github.com/KishanViramgama/ktor-api-client/blob/master/ktor-api.postman_collection.json)
<br><br>
## 📱 Mobile Application Authentication Demo

Looking for a **mobile application** that demonstrates **user login and registration** using the same Ktor API provided in this project? Check out the companion Android demo app:

🔗 **Android Demo App (Login & Register):**  
[https://github.com/KishanViramgama/LoginRegister](https://github.com/KishanViramgama/LoginRegister)

This Android project is fully integrated with this API and showcases:

- ✅ User **registration** flow  
- 🔐 User **login** handling  
- 🔗 **Ktor client** API integration  
- 🎨 **Jetpack Compose UI** (if applicable)

Clone and run the app on your emulator or device to experience the complete authentication flow in action.

## 🤝 Contributing

Pull requests and issues are welcome! Feel free to fork the project and enhance it.

## 📣 Let's Connect

Follow me on GitHub to stay updated with Kotlin, Ktor, and backend projects! 🌟

## ☕ Support

If you like this library and want to support my work, consider buying me a coffee.

<p align="center">
  <a href="https://paypal.me/KishanViramgama?country.x=IN&locale.x=en_GB" target="_blank">
    <img src="https://img.shields.io/badge/☕-Buy%20Me%20a%20Coffee-orange?style=for-the-badge" />
  </a>
</p>
