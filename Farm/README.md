# 🚜 Farm Management Simulation

## 📝 Project Description

This Java project simulates a farm management system with animal processing capabilities. It demonstrates object-oriented programming concepts through a detailed simulation of farm animals, butchering processes, and meat type transformations. 🌾🔪

## 🏗️ Project Structure

The project consists of several key classes:
- `Animal` : Represents individual animals with properties like age, weight, owner, and species
- `Butcher` : Manages meat processing and animal slaughtering
- `Farm` : Handles farm operations and animal management
- `MeatType` : An enum representing different meat processing stages
- `Specie` : Defines available animal species

## ✨ Features

- Create and manage farm animals 
- Track animal properties (age, weight, owner) 
- Simulate animal lifecycle (feeding, slaughtering) 
- Meat processing with multiple stages (Steak → Cubed → Minced) 🥩
- Butcher authorization system 🛡️
- Detailed farm and animal information display 

## 🚀 How to Run

1. Compile all Java files in the `Farm` package
2. Run the `ButcherShop` class as the main entry point

## 🎬 Example Usage

The `ButcherShop` main method demonstrates:
- Creating a farm 🏡
- Adding animals 🐑
- Killing animals 💀
- Processing meat 🥩
- Handling butcher permissions 🤵

## 👥 Contributors

- **[Ali Haydar Sucu](https://github.com/alihaydarsucu)**
- **[H. Salih Toker](https://github.com/SalihToker)**


## 📌 Notes

- Only the first butcher created is an "allowed" butcher 👨‍🍳
- Animals can only be processed after being killed 🔪
- Meat can be downgraded from Steak → Cubed → Minced 🍖
