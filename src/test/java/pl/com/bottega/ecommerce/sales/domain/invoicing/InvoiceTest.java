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

public class InvoiceTest {

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
	public void testGrosAndNetCalculation() {
		ClientData client = new ClientData(Id.generate(), "New Client");
		Invoice invoice = new InvoiceFactory().create(client);
		TaxPolicy taxPolicy = new DefaultTaxPolicy();

		ProductData product = new ProductData(Id.generate(), new Money(10d), "Product1", ProductType.DRUG, new Date());
		int quantity = 7;
		Money net = new Money(15d);
		Tax tax = taxPolicy.calculateTax(product.getType(), net);
		invoice.addItem(new InvoiceLine(product, quantity, net, tax));

		product = new ProductData(Id.generate(), new Money(5d), "Product1", ProductType.DRUG, new Date());
		quantity = 10;
		net = new Money(10d);
		tax = taxPolicy.calculateTax(product.getType(), net);
		invoice.addItem(new InvoiceLine(product, quantity, net, tax));

		product = new ProductData(Id.generate(), new Money(4.5d), "Product1", ProductType.DRUG, new Date());
		quantity = 22;
		net = new Money(5d);
		tax = taxPolicy.calculateTax(product.getType(), net);
		invoice.addItem(new InvoiceLine(product, quantity, net, tax));

		assertThat(invoice.getItems().size(), is(3));
		assertThat(invoice.getGros(), is(new Money(31.5)));
		assertThat(invoice.getNet(), is(new Money(30.0)));

	}
}
