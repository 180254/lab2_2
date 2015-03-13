package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class BookKeeperTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testIssuance_grosAndNetCalculation() {
		InvoiceFactory invoiceFactory = new InvoiceFactory();
		BookKeeper bookKeeper = new BookKeeper(invoiceFactory);
		ClientData client = new ClientData(Id.generate(), "New Client");
		InvoiceRequest request = new InvoiceRequest(client);

		ProductData product = new ProductData(Id.generate(), new Money(10d), "Product1", ProductType.DRUG, new Date());
		RequestItem item = new RequestItem(product, 100, new Money(123.5d));
		request.add(item);

		product = new ProductData(Id.generate(), new Money(2.5), "Product2", ProductType.FOOD, new Date());
		item = new RequestItem(product, 100, new Money(13.6d));
		request.add(item);

		product = new ProductData(Id.generate(), new Money(22.5), "Product3", ProductType.FOOD, new Date());
		item = new RequestItem(product, 100, new Money(133.63d));
		request.add(item);

		System.out.println(request.getClientData());
		Invoice invoice = bookKeeper.issuance(request, new DefaultTaxPolicy());

		assertThat(invoice.getItems().size(), is(3));
		assertThat(invoice.getGros(), is(new Money(287.21)));
		assertThat(invoice.getNet(), is(new Money(270.73)));

	}

}