package filematch_package_PM;
// Reading a file of objects sequentially with ObjectInputStream
// and displaying each record.
import java.io.EOFException;     
import java.io.IOException;      
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadAllFiles
{
	//create instances for input files
	private static ObjectInputStream inputOld;
	private static ObjectInputStream inputTrans;
	private static ObjectInputStream inputNew;

	public static void main(String[] args)
	{
		openFile();
		readRecords();
		closeFile();
	} 

	// opens previously specified files
	public static void openFile()
	{
		try 
		{
			inputOld = new ObjectInputStream(          
				Files.newInputStream(Paths.get("oldmast.ser")));
			inputTrans = new ObjectInputStream(          
				Files.newInputStream(Paths.get("trans.ser")));
			inputNew = new ObjectInputStream(          
				Files.newInputStream(Paths.get("newmast.ser")));
		} 
		catch (IOException ioException)
		{
			System.err.println("Error opening file.");
			System.exit(1);
		} 
	}

	//prints all the records
	public static void readRecords()
	{
		System.out.println("Old Master File:");
		System.out.printf("%-10s%-12s%-12s%10s%n", "Account",
			"First Name", "Last Name", "Balance");

		try // read record from Old Master file
		{
			while (true) // loop until there is an EOFException
			{
				AccountRecordSerializable record1 = (AccountRecordSerializable) inputOld.readObject();
				System.out.printf("%-10d%-12s%-12s%10.2f%n",  
				record1.getAccount(), record1.getFirstName(), 
				record1.getLastName(), record1.getBalance());
			} 
		}
		catch (EOFException endOfFileException)
		{
			System.out.printf("%nNo more records%n\n");
		} 
		catch (ClassNotFoundException classNotFoundException)
		{
			System.err.println("Invalid object type. Terminating.");
		} 
		catch (IOException ioException)
		{
			System.err.println("Error reading from file. Terminating.");
		}
		System.out.println("Transactions:");
		System.out.printf("%-11s%-5s%n", "Account", "Transaction");
		
		try // read record from Transaction file
		{
			while (true) // loop until there is an EOFException
			{
				TransactionRecord record2 = (TransactionRecord) inputTrans.readObject();
				System.out.printf("%-15d%-10.2f%n",  
				record2.getAccount(), record2.getTransaction());
			} 
		}
		
		catch (EOFException endOfFileException)
		{
			System.out.printf("%nNo more records%n\n");
		} 
		catch (ClassNotFoundException classNotFoundException)
		{
			System.err.println("Invalid object type. Terminating.");
		} 
		catch (IOException ioException)
		{
			System.err.println("Error reading from file. Terminating.");
		} 
		
		System.out.println("New Master File:");
		
		try // read record from New Master file
		{
			while (true) // loop until there is an EOFException
			{
				AccountRecordSerializable record3 = (AccountRecordSerializable) inputNew.readObject();
				System.out.printf("%-10d%-12s%-12s%10.2f%n",  
				record3.getAccount(), record3.getFirstName(), 
				record3.getLastName(), record3.getBalance());
			} 
		}
		catch (EOFException endOfFileException)
		{
			System.out.printf("%nNo more records%n\n");
		} 
		catch (ClassNotFoundException classNotFoundException)
		{
			System.err.println("Invalid object type. Terminating.");
		} 
		catch (IOException ioException)
		{
			System.err.println("Error reading from file. Terminating.");
		}
	} // end method readRecords

	// close files and terminate application
	public static void closeFile()
	{
		try
		{
			if (inputOld != null)
				inputOld.close();
			if (inputTrans != null)
				inputTrans.close();
			if (inputNew != null)
				inputNew.close();
		} 
		catch (IOException ioException)
		{
			System.err.println("Error closing file. Terminating.");
			System.exit(1);
		} 
	} 
} // end class ReadAllFiles