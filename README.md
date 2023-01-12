This project is mostly just for personal use, but I have added an installer that I made for the program in the releases section, so feel free to try it out if you'd like!

The basic premis is for the program to act as a sort of register for any and all financial transactions one may make. It does this by keeping a spreadsheet of each year
stored in the 'data' folder of it's installation directory (this folder is kept locally only, so you don't have to worry about anything you enter getting stored on
some datacenter or something). So for this year, it would keep a spreadsheet simply named '2023'. When a new year comes, say moving from 2023 -> 2024, the program will
automatically create a new spreadsheet for the appropriet year, set it up so that it is formatted correctly, and use that one for the rest of the year. The earlier
year's spreadsheet is kept so that you may keep a record of your financial transactions for that year, which can be helpful primarily for tax purposes.

Also, since the program is meant to be used to keep track of your transactions for the entirety of a year, if you don't make any purchases or deposits for a few days,
and therefore don't use the program at all during that time, when you next launch the program, it will see the date difference between your last entry and the current
date is more than one day, and will create default* entries for all of the missed days. So if you last used the program on the 2nd, and opened it again on the 9th, it
would autofill information for the entries on the 3rd through to the 8th, with the 9th being the active date to enter information for.

*'Default information' just refers to $0.00 for deposits and expenditures, the same remaining balance as the day's starting balance (which is obtained from looking at
the previous entries' remaining balance), and 'No purchases for this day.' as the purchase summary.

If you want an idea of what the user interface is like, I've attatched boilerplate images of the program in-use for you to look through.

![FMS_Showcase](https://user-images.githubusercontent.com/67993317/211433977-c9705d81-e63d-4a3d-af5f-df1aa8aac733.png)

![Deposit_Showcase](https://user-images.githubusercontent.com/67993317/211434007-12465480-b2c1-4751-851b-daad612002b7.png)

![Expenditure_Showcase](https://user-images.githubusercontent.com/67993317/211434027-42bce8d1-e3b7-482f-9e14-6e1b9138052f.png)

