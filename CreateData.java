//File CreateData.java was provided by the Professor.

package filematch_package_PM;
// Create data to put into an account file and a transactions file.
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class CreateData
{
	private static ObjectOutputStream outOldMaster, outTransaction;

	public static void main(String[] args)
	{
		try
		{
			try
			{
				// File streams for output files.
				outOldMaster = new ObjectOutputStream(
					new FileOutputStream("oldmast.ser"));
				
				outTransaction = new ObjectOutputStream(
					new FileOutputStream("trans.ser"));
			} 
			catch (IOException io)
			{
				System.err.println("Error opening the file.");
			} 
		
			try
			{      
				outOldMaster.writeObject(new AccountRecordSerializable(
					100, "Alan", "Jones", 348.17));
				outOldMaster.writeObject(new AccountRecordSerializable(
					300, "Mary", "Smith", 27.19));
				outOldMaster.writeObject(new AccountRecordSerializable(
					500, "Sam", "Sharp", 0.00));
				outOldMaster.writeObject(new AccountRecordSerializable(
					700, "Suzy", "Green", -14.22));
		
				outTransaction.writeObject(
					new TransactionRecord(100, 27.14));
				outTransaction.writeObject(
					new TransactionRecord(300, 62.11));
				outTransaction.writeObject(
					new TransactionRecord(300, -10.00));
				outTransaction.writeObject(
					new TransactionRecord(400, 100.56));
				outTransaction.writeObject(
					new TransactionRecord(900, 82.17));
			} 
			catch (IOException io)
			{
				System.out.println("Error writing to the files.");
				System.exit(1);
			} 
		}
		finally // close the files
		{
			try
			{
				if (outTransaction != null)
					outTransaction.close();

				if (outOldMaster != null)
					outOldMaster.close();
			} 
			catch (IOException io)
			{
				System.err.println("Error closing the files.");
				System.exit(1);
			} 
		} 
	} 
} // end class CreateData

