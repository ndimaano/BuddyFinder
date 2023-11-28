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
