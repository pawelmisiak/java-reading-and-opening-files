// Child class to AccountRecordSerializable, used for storing records as objects.
// The class hold only part of necessary information provided in the parent class.
package filematch_package_PM;
import java.io.Serializable;

public class TransactionRecord extends AccountRecordSerializable
{
	private int account;
	private double transaction;
	
	// initializes an TransactionRecord with default values
	public TransactionRecord()
	{
		this(0,0.0);
	}
	
	// initializes an TransactionRecord with provided values
	public TransactionRecord (int account, double transaction)
	{
		this.account = account;
		this.transaction = transaction;
	}
	
	// get account number
	public int getAccount() 
	{ 
		return account;
	} 
	
	// get the transaction
	public double getTransaction()
	{
		return transaction;
	}
}