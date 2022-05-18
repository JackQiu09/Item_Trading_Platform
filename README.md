Running Our Program
List of Phase 2 Extensions We Chose To Do
 (Only required to do 3 because of # of members)
* Allow users to enter their home city when creating an account, so that the system will only show them other users in the same city.
* Monetize the system. In other words, give users the option to sell or trade items.
* Have the automated trade suggestion always return the most reasonable trade, even if it's not an exact match.


*NOTE - We use a .ser file to save our information. You must use the program to test features.
BEFORE YOU RUN THE PROGRAM:
MAKE SURE YOU OPEN THE PROJECT IN GROUP0050 FOLDER INSTEAD OF PHASE 2 FOLDER, THE DIRECTORY OF THE DATABASE IS SET IN GROUP0050 FOLDER, IF YOU OPEN PROJECT IN PHASE 2 THE PROGRAM WON’T RUN.

Run Trading Main to start our program


Account Status
Keep in mind that client user accounts can be in four different status: “active”, “pending”, “frozen”, “invisible”
“active”: user account has access to full functionality
“pending”: user account that exceeds the thresholds will be set to ”pending”. “pending” accounts will not be able to access the trading system, and will be awaiting for the admin to freeze.
“frozen”: pending user account will be set to frozen manually by admin user.
“Invisible”: user can send/reactivate his or her account into vacation mode, in which the account will not be able to trade.


Start Menu
You will see multiple prompts. Type in the corresponding ‘#’ to do the action. 
You can login with username ‘user’ and password ‘password’ in order to get into a default client user account with your home city set as toronto. To get into the admin account the username is ‘admin’ and password is ‘password’


Sign up allows you to create a new account which automatically logs you in after account creation. In order to see trade with users you must be in the same city as them. Leave blank if you want to trade with everyone.


Look around is our visitor system where you don’t need to login but can look around and access information in our program like the inventories.


Exit to exit program.


Client Menu
*NOTE - properly log out of account to save information for next run time
Prompt
1. Look for things to trade
2. Browse Pending Transactions
3. View 3 Most Recent Traded Items
4. View 3 Most Frequent Trade Partners
5. Browse Pending Appointments
6. Browse Confirmed Appointments
7. View your wish list
8. View your lending list
9. Request to add an item
10. Request admin to unfreeze account
11. View your Threshold limits
12. Complete a transaction
13. Send your account to vacation(invisible) status
14. Suggest Trade
15. Change your home location
16. Make a deposit
17. View account status
18. Browse Completed Transactions
19. Activate account from vacation
20. Log Out


More Info
1 - Opens up trading menu


2 - Straight forward, outputs transaction ticket that still needed to be confirmed if you want to see newly created transaction ticket after properly confirmed appointments


3 - Allows you to see top 3 most recent traded items


4 - Allows you to see top 3 most frequent trade partners. If there are not enough trade partners, it will display what it can. 


5 - Straight forward, output appointments still needed to be confirmed if you want to see newly created appointments 


6 - Straight forward, output appointments confirmed by both users if you want to see if confirmation of appointment was successful.


7 - Straight forward, output wishlist whenever you want to see adding to wish list works 


8 -  Straight forward, output lendinglist (inventory) whenever you want to see adding to inventory works 


9 - will allow you to add items into your program. To actually add the item into the inventory you must confirm through an admin using 2 in the admin menu. Just copy and paste the id instead of typing inorder to confirm the item. You can check a user’s inventory by choosing 8. After successfully adding items into the user’s inventory, you can begin to trade. 


10 - When admin has frozen an account, you may request to unfreeze


11 - Displays the threshold limits the user has. Useful in seeing admin changes to thresholds.


12 - Done to confirm transactions and finish trades


13 - Send your account to vacation(invisible) status


14 - Suggesting trade to users based on their past transaction history.


15 - Change your home location in order to test out that home city extension works properly.


16 -  A client user can request to deposit money into the system through here. An admin user must approve this deposit before it is completed.


17 - Display your account status to check if you are active, frozen or on vacation(invisible)


18 - View the completed transaction here to see that confirmation (12) was successful


19 -  Allows the user to reactivate the account from “invisible” to “active”.


20 - Remember to log out inorder to properly save changes into the save file for future runs of the program


Trading, lending or Borrowing (Trade Menu)
*NOTE - The date you add MUST match the format given. (yyyy-MM-dd-HH:mm) 
(year-month-date-hour-minute)
*NOTE - just copy and paste the id instead of typing
*NOTE - if you exceed transaction limit you can no longer trade (It times based off transaction ticket date and is based on if it is a week old, it adds to the counter


Prompt
1. Add an item to wishlist
2. Request to Trade
3. Request to Borrow
4. Lend your items
5. Request to Buy
7. Edit/confirm/decline an appointment
8. Return to Client menu


1 - Straight forward, just choose to add an item into the wishlist from the generated inventory list you are given.
2 - Start a two way trade. You can make it permanent or temporary. Temporary will generate a new appointment with a new date (a month later) in order to retrade again. You must confirm like a regular appointment to confirm the transaction. Permanent doesn’t generate an additional appointment. (the first item is from the other person’s inventory, then put in the date, then put in the item from your inventory) 


3 - You are first prompted for an auto suggestion, the auto suggestion is based on if your lending list has something in the person you want to borrow a wishlist. If no suggestion is possible proceed to regular borrowing. 


4 - Straight forward - like borrow expect without the auto suggestion.


5 - Once you have an item in mind, enter its id here and suggest a price, meeting place and time to make a trade. The other trader will come back with counter offers, and the fun begins!


6 - Here, you request to sell. That is all.


7 - After choosing either 2,3,4 in this menu, this is where you move on to confirming the transaction but first setting up an appointment where you can edit, confirm or delete before the transaction moves to the next step of creating a transaction ticket.


8 - Return to the client menu. Logout to guarantee saving of files!


Admin Menu
When logged in as an admin, you will see a list of options. Type in the corresponding “#” to select what operations you would like to perform. Here are the list of options:


1 - View Pending Users: Shows a list of users whose account status is set to “pending”.


2 - View Pending Items: Shows a list of items that client users want to add into their inventory.


3 - Review Pending Transactions: Input the username to see that user’s ongoing transactions.


4 - Change Thresholds: Admins will have the options to: change a threshold to one specific user or to change threshold to all client users. Note: Threshold name should be one of  "incompleteLimit"/ "transactionLimit"/"lentBorrowDiff".


5 - View all client users: View all client users in the system.


6 - Unfreeze User: Given a list of users that are in “frozen” status and have requested to unfreeze, enter the respective user id to unfreeze them.


7 - Freeze User: Given a list of “pending” status users, enter the respective username to freeze their account.


8 - Display All Admins: View all the admins of the system.


9 - View User Threshold: View each user’s threshold.


10 - Approve a deposit: 


11 - Undo Action:


12 - Create admin account (This account will no longer be initial account, so it can’t create subsequent admin accounts) This does not log you into that account. You must re login to access the new account


13 - Log Out: log out to the starting menu.
