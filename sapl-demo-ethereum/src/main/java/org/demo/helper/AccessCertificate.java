package org.demo.helper;

import java.io.IOException;
import java.math.BigInteger;

import org.demo.helper.contracts.Device_Operator_Certificate;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

public class AccessCertificate {

	private static final String ACCREDITATION_AUTHORITY = "0x3924F456CC0196ff89AAbbD6192289a9B37De73A";

	private static final String ACCREDITATION_AUTHORITY_PRIVATE_KEY = "7bb90c8b20c4bfdc5833c5e94b36ec3fa050346f04441878a323eec3483960c4";

	private static final String GRADUATE = "0xE5a72C7Fa4991920619edCf25eD8828793045A53";

	private static final String DOC_CONTRACT = "0x9CDD57201DB1110A09d44F675cA00acaB62E5cE7";

	private static final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);

	private static final BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);

	public static void issueCertificate(String address) {
		Web3j web3j = Web3j.build(new HttpService());
		Credentials credentials = Credentials.create(ACCREDITATION_AUTHORITY_PRIVATE_KEY);
		Device_Operator_Certificate contract = Device_Operator_Certificate.load(DOC_CONTRACT, web3j, credentials,
				new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
		try {
			System.out.println(web3j.ethCoinbase().send().getAddress());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contract.issueCertificate(address).sendAsync();
	}

	public static void revokeCertificate(String address) {
		Web3j web3j = Web3j.build(new HttpService());
		Credentials credentials = Credentials.create(ACCREDITATION_AUTHORITY_PRIVATE_KEY);
		Device_Operator_Certificate contract = Device_Operator_Certificate.load(DOC_CONTRACT, web3j, credentials,
				new StaticGasProvider(GAS_PRICE, GAS_LIMIT));
		contract.revokeCertificate(address).sendAsync();
	}
}