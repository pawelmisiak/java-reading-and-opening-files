/*
 Day_PawelMisiak.java
 Project name: 	Assignment 2
 Programmer:    Pawel Misiak
 Instructor:    SWEETY VARGHESE
 Class:         Java Application Development
 Date:          03/02/17
 
 Software Development Method
 
 1) Problem:
	There should be a master file (eg: oldmaster.ser) containing details about each account. As transactions occur, data is entered into a 	transaction file.(Eg: trans.ser )
	Your program should mimic a file matching process that occurs at the end of a business term period, where these transactions from the 	transaction file are applied to the old master file and it gets rewritten as a new master file ( eg: newmaster.ser")

 A)  Create a class FileMatch to perform file matching functionality. This class should have methods that read the old master and 			transaction files.
Case 1) When a match occurs (records with same account number appear in both master file and transaction file) update the current balance 		in the master record and add records to newmaster file. Account for multiple transaction records with same account number. 
Case 2) When there's a master record for a particular account but no corresponding transaction record , write the master record to 				newmaster file. 
Case 3) When there is a transaction record but no corresponding master record print to a log file the message " Unmatched transaction 			record for account number...". The Log file should be a text file called log.txt 

 B)  Make sure you have programs that create test data for the master and transaction files. (Refer Sample data given above) You could also 	instead use the sample CreateData.java file I have attached which loads these two files. 

 C)  Also write a program to read data stored in all the three files (oldmaster, trans, newmaster) to check the file match results. 

 2) User input:   
	User can modify inputs such as Account Number, First Name, Last Name and Balance in CreateData.java file.
 
 3) Example of an output:

	Old Master File:
	Account   First Name  Last Name      Balance
	100       Alan        Jones           348.17
	300       Mary        Smith            27.19
	500       Sam         Sharp             0.00
	700       Suzy        Green           -14.22

	No more records

	Transactions:
	Account    Transaction
	100            27.14     
	300            62.11     
	300            -10.00    
	400            100.56    
	900            82.17     

	No more records

	New Master File:
	100       Alan        Jones           375.31
	300       Mary        Smith            79.30
	500       Sam         Sharp             0.00
	700       Suzy        Green           -14.22

	No more records
 */

package filematch_package_PM;

import java.io.EOFException;     
import java.io.IOException;      
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class FileMatch extends CreateData
{
	//Creating instances for input and output files
	private static ObjectInputStream inputOld;
	private static ObjectInputStream inputTrans;
	private static ObjectOutputStream outNewMaster;
	private static FileOutputStream outLog;
	
	
	public static void main(String[] args) 
	{
		openFiles();
		compareFiles();
		findUnregistered();
		closeFiles();
	}
	
	/* The method opens and creates files*/
	public static void openFiles()	 
	{
		try // opening Old Master file, Transaction file will be opened later (explained on line #121
		{
			inputOld = new ObjectInputStream(          
				Files.newInputStream(Paths.get("oldmast.ser")));
		} 
		catch (IOException ioException)
		{
			System.err.println("Error opening file.");
			System.exit(1);
		} 
		try // creating New Master and Log files
		{
			outLog = new FileOutputStream("log.txt");
			
			outNewMaster = new ObjectOutputStream(
			new FileOutputStream("newmast.ser"));
			
		}
		catch (IOException io)
		{
			System.err.println("Error opening the file.");
		}

	}
	
	// This method will compare Old Master with Transaction and create New Master file
	public static void compareFiles() 
	{
		try 
		{
			while (true) // loop until there is an EOFException
			{
				AccountRecordSerializable record = (AccountRecordSerializable) inputOld.readObject();
				
				try // Transaction file need to be reopened each time at the 
					// beggining of first while loop (it gets closed automatically)
				{
					inputTrans = new ObjectInputStream(          
					Files.newInputStream(Paths.get("trans.ser")));
				} 
				catch (IOException ioException)
				{
					System.err.println("Error opening file.");
					System.exit(1);
				}

				AccountRecordSerializable temp = record; 	// temporary file to hold value from the old master file
															// to new master file
				try 
				{
					while (true) // loop until there is an EOFException
					{
						TransactionRecord transRecord = (TransactionRecord) inputTrans.readObject();
						if (temp.getAccount() == transRecord.getAccount()) // temporary file will get modified
						{													// if necessary (if accounts match)
							temp.setBalance(temp.getBalance()+transRecord.getTransaction());
						}
					}
				}
						catch (EOFException endOfFileException)
						{
							
						} 
						catch (IOException ioException)
						{
							System.err.println("Error reading from file Account. Terminating.");
						} 
						
						// write information to the new Master File
						outNewMaster.writeObject(new AccountRecordSerializable(temp.getAccount(),temp.getFirstName(),temp.getLastName(),temp.getBalance()));
			} 
		}
		catch (EOFException endOfFileException)
		{
			System.out.printf("");
		} 
		catch (ClassNotFoundException classNotFoundException)
		{
			System.err.println("Invalid object type. Terminating.");
		} 
		catch (IOException ioException)
		{
			System.err.println("Error reading from file. Terminating.");
		} 
	}
	
	//method will compare again the Transaction file and Old Master file looking for transactions
	//that do not have coresponding account in Master file
	public static void findUnregistered()
	{
		PrintStream logOutput = new PrintStream(outLog);
		try // open file
		{
			inputTrans = new ObjectInputStream(          
				Files.newInputStream(Paths.get("trans.ser")));
		} 
		catch (IOException ioException)
		{
			System.err.println("Error opening file.");
			System.exit(1);
		} 
		
		try 
		{
			
			while (true) // loop until there is an EOFException
			{
				
				
				TransactionRecord transRecord = (TransactionRecord) inputTrans.readObject();
				try // Transaction file need to be reopened each time at the 
					// beggining of first while loop. It gets closed when EOF
				{
					inputOld = new ObjectInputStream(          
					Files.newInputStream(Paths.get("oldmast.ser")));
				} 
				catch (IOException ioException)
				{
					System.err.println("Error opening file.");
					System.exit(1);
				}
				
				try 
				{
					while (true) // loop until there is an EOFException
					{
							// account numbers are being compared until no match is found
							AccountRecordSerializable record = (AccountRecordSerializable) inputOld.readObject();
							while (transRecord.getAccount() == record.getAccount())
							{
								transRecord = (TransactionRecord)inputTrans.readObject();
							}
					}
				}
				
				catch (EOFException endOfFileException)
				{
									
				} 
				catch (IOException ioException)
				{
					System.err.println("Error reading from file Account. Terminating.");
				} 
					// writes out message to the Log file
					logOutput.println("Unmatched transaction record for account number " + transRecord.getAccount());

				}
				
			}			
		catch (EOFException endOfFileException)
		{
			System.out.printf("");
		} 
		catch (ClassNotFoundException classNotFoundException)
		{
			System.err.println("Invalid object type. Terminating.");
		} 
		catch (IOException ioException)
		{
			System.err.println("Error reading from file. Terminating.");
		} 
	}
	//the function will make sure that all the files are closed
	public static void closeFiles()
	{
			try
			{
				if (outLog != null)
				outLog.close();
				if (outNewMaster != null)
					outNewMaster.close();
				if (inputOld != null)
				inputOld.close();
				if (inputTrans != null)
				inputTrans.close();
			} 
			catch (IOException io)
			{
				System.err.println("Error closing the files.");
				System.exit(1);
			} 
		
	} 
}