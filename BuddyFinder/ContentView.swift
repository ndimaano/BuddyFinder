//
//  ContentView.swift
//  BuddyFinder
//
//  Created by Xichen Gao on 11/28/23.
//

import SwiftUI

struct ContentView: View {
    @State private var isLoginPresented = false
    @State private var isRegisterPresented = false
    @State private var showBuddy = false
    @State private var buddyName = ""
    @State private var buddyCoordinates = (x: 0.0, y: 0.0)

    var body: some View {
        NavigationView {
            VStack {
                Button(action: {
                                isLoginPresented = true
                            }) {
                                Text("Login")
                                    .padding()
                                    .foregroundColor(.white)
                                    .background(Color.blue)
                                    .cornerRadius(10)
                            }
                            .sheet(isPresented: $isLoginPresented) {
                                LoginPage()
                            }
                            Spacer().frame(height: 20)
                            Button(action: {
                                isRegisterPresented = true
                            }) {
                                Text("Register")
                                    .padding()
                                    .foregroundColor(.white)
                                    .background(Color.green)
                                    .cornerRadius(10)
                            }
                            .sheet(isPresented: $isRegisterPresented) {
                                RegisterPage()
                            }
                            Button(action: {
                                    // For now, Generate a random buddy. Otherwise, pull one from queue
                                    buddyName = "User \(Int.random(in: 1...100))"
                                    buddyCoordinates = (x: Double.random(in: -100...100), y: Double.random(in: -100...100))
                                    showBuddy = true
                                }) {
                                    Text("Find Buddy")
                                        .padding()
                                        .foregroundColor(.white)
                                        .background(Color.orange)
                                        .cornerRadius(10)
                                }
                                if showBuddy {
                                    Text("Buddy: \(buddyName)")
                                    Text("Coordinates: (\(buddyCoordinates.x), \(buddyCoordinates.y))")
                                }
                Spacer()
            }
            .padding()
            .navigationBarTitle("Buddy Finder")
        }
    }
    private func loginButtonTapped() {
        isLoginPresented = true
    }
    private func registerButtonTapped() {
        isRegisterPresented = true
    }
}
