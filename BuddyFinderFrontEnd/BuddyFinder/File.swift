//
//  File.swift
//  BuddyFinder
//
//  Created by Nicolas Dimaano on 12/1/23.
//

//import Foundation
//
//class Chatroom {
//    private var messages: [String] = []
//    private let lock = NSLock()
//
//    func sendMessage(_ message: String) {
//        lock.lock()
//        defer { lock.unlock() }
//
//        messages.append(message)
//    }
//
//    func receiveMessages() -> [String] {
//        lock.lock()
//        defer { lock.unlock() }
//
//        let receivedMessages = messages
//        messages.removeAll()
//        return receivedMessages
//    }
//}
//
//class User {
//    let name: String
//    let chatroom: Chatroom
//
//    init(name: String, chatroom: Chatroom) {
//        self.name = name
//        self.chatroom = chatroom
//    }
//
//    func sendMessage(_ message: String) {
//        print("\(name) sending: \(message)")
//        chatroom.sendMessage("\(name): \(message)")
//    }
//
//    func receiveMessages() {
//        while true {
//            let receivedMessages = chatroom.receiveMessages()
//            for message in receivedMessages {
//                print("\(name) received: \(message)")
//            }
//            Thread.sleep(forTimeInterval: 1) // Simulate polling every second
//        }
//    }
//}
//
//// Usage
//let chatroom = Chatroom()
//let user1 = User(name: "User1", chatroom: chatroom)
//let user2 = User(name: "User2", chatroom: chatroom)
//
//let user1Thread = Thread {
//    user1.receiveMessages()
//}
//
//let user2Thread = Thread {
//    user2.receiveMessages()
//}
//
//user1Thread.start()
//user2Thread.start()
//
//user1.sendMessage("Hello, User2!")
//user2.sendMessage("Hi, User1!")
//
//Thread.sleep(forTimeInterval: 5) // Simulate a chat session for 5 seconds
//
//user1Thread.cancel()
//user2Thread.cancel()



