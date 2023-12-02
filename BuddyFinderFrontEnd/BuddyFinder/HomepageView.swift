import SwiftUI

struct HomepageView: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @State private var chatNavigationActive: Bool = false
    @State private var isPresented = false
    @State private var selectedName: String? = nil
    var body: some View {
        NavigationView {
            VStack {
                List(["User 1", "User 2", "User 3", "User 4", "User 5"], id: \.self) { name in
                    HStack {
                        Text(name)
                            .onTapGesture {
                                selectedName = name
                                chatNavigationActive = true
                            }
                        Spacer()
                        Button("Start Chat") {
                            isPresented = true
                        }
                        .background(
                            Group {
                                if isPresented {
                                    NavigationLink(
                                        destination: ChatPage(userName: selectedName ?? ""),
                                        label: { EmptyView() }
                                    )
                                }
                            }
                        )
                        
                        if selectedName == name {
                            Button(action: {
                                navigateToChat(userName: selectedName!)
                            }) {
                                Text("Chat")
                                    .foregroundColor(.white)
                                    .padding(.vertical, 8)
                                    .padding(.horizontal, 16)
                                    .background(Color.blue)
                                    .cornerRadius(10)
                            }
                        }
                    }
                }
                .listStyle(GroupedListStyle())
                .navigationBarTitle("Home")
                Spacer()
                Button(action: {
                    self.presentationMode.wrappedValue.dismiss()
                }) {
                    Text("Back")
                        .foregroundColor(.white)
                        .padding(.vertical, 8)
                        .padding(.horizontal, 16)
                        .background(Color.blue)
                        .cornerRadius(10)
                }
                .padding(.bottom)
            }
        }
    }
    private func navigateToChat(userName: String) {
        chatNavigationActive = true
    }
}
