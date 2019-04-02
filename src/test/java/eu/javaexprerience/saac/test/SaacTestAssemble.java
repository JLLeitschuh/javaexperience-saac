package eu.javaexprerience.saac.test;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.saac.SaacEnv;
import eu.javaexperience.saac.client.SaacContainer;
import eu.javaexprerience.saac.test.SaacFunctionsForTest.ActorDescriptor;
import eu.javaexprerience.saac.test.SaacFunctionsForTest.EvalContext;
import eu.javaexprerience.saac.test.SaacFunctionsForTest.ModificationCommand;
import eu.javaexprerience.saac.test.SaacFunctionsForTest.WellKnownAttributes;
import eu.javaexprerience.saac.test.SaacFunctionsForTest.WellKnownEntityAttribute;
import eu.javaexprerience.saac.test.SaacTestTools.SaacTestComponents;

import static eu.javaexperience.saac.client.SaacContainer.*;

public class SaacTestAssemble
{
	public static SaacContainer c(String name, SaacContainer... cnts)
	{
		return SaacContainer.create
		(
			SaacFunctionsForTest.class,
			name,
			cnts
		);
	}
	
	@Test
	public void testWrapConstAsGetFunction_and_singleVarargsArrayWrap()
	{
		SaacContainer f = c
		(
			"doWhen",
			c("isAfterDate", create(System.currentTimeMillis()-15000)),
			
			//direct values being wrapped to getter function
			c
			(
				"newEntryWithAttributes",
				createArray().addArgument
				(
					//using single parameter for for vararg params
					c("newFreeAttribute", create("startValue"), create("myValue"))
				)
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNotNull(cmd);
		ActorDescriptor ad = (ActorDescriptor) cmd;
		
		assertEquals(1, ad.attributes.size());
		assertEquals("startValue", ad.attributes.get(0).getName());
		assertEquals("myValue", ad.attributes.get(0).getValue());
	}
	
	@Test
	public void testWrapConstAsGetFunction_and_multiVarargUsingArrayWrappedAsVaragaArray()
	{
		SaacContainer f = c
		(
			"doWhen",
			c("isAfterDate", create(System.currentTimeMillis()-15000)),
			
			//direct values being wrapped to getter function
			c
			(
				"newEntryWithAttributes",
				//using an array declaration for vararg params
				createArray().addArgument
				(
					c("knownAttribute", create(WellKnownAttributes.COLOR.name()), create("red")),
					c("newFreeAttribute", create("startValue"), create("myValue"))
				)
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNotNull(cmd);
		ActorDescriptor ad = (ActorDescriptor) cmd;
		
		assertEquals(2, ad.attributes.size());
		assertEquals("COLOR", ad.attributes.get(0).getName());
		assertEquals("red", ad.attributes.get(0).getValue());
		
		assertEquals("startValue", ad.attributes.get(1).getName());
		assertEquals("myValue", ad.attributes.get(1).getValue());
	}
	
	@Test
	public void testStringToEnum()
	{
		SaacContainer f = c
		(
			"doWhen",
			c("isAfterDate", create(System.currentTimeMillis()-15000)),
			c
			(
				"newEntryWithAttributes",
				//also test that name being casted to enum type
				c("knownAttribute", create(WellKnownAttributes.COLOR.name()), create("red"))
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNotNull(cmd);
		ActorDescriptor ad = (ActorDescriptor) cmd;
		
		assertEquals(1, ad.attributes.size());
		assertEquals(WellKnownAttributes.COLOR, ((WellKnownEntityAttribute)ad.attributes.get(0)).attr);
		assertEquals("COLOR", ad.attributes.get(0).getName());
		assertEquals("red", ad.attributes.get(0).getValue());
	}
	
	@Test
	public void testUsingCreateFunctionVarargsMultiParam()
	{
		SaacContainer f = c
		(
			"doWhen",
			c("isAfterDate", create(System.currentTimeMillis()-15000)),
			c
			(
				"funcNewEntryWithAttributes",
				createArray().addArgument
				(
					c("knownAttribute", create(WellKnownAttributes.COLOR.name()), create("red")),
					c("newFreeAttribute", create("startValue"), create("myValue"))
				)
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNotNull(cmd);
		ActorDescriptor ad = (ActorDescriptor) cmd;
		
		assertEquals(2, ad.attributes.size());
		assertEquals(WellKnownAttributes.COLOR, ((WellKnownEntityAttribute)ad.attributes.get(0)).attr);
		assertEquals("COLOR", ad.attributes.get(0).getName());
		assertEquals("red", ad.attributes.get(0).getValue());
		
		assertEquals("startValue", ad.attributes.get(1).getName());
		assertEquals("myValue", ad.attributes.get(1).getValue());
		
	}
	
	@Test
	public void testUsingCreateFunctionVarargsSingleParam()
	{
		SaacContainer f = c
		(
			"doWhen",
			c("isAfterDate", create(System.currentTimeMillis()-15000)),
			c
			(
				"funcNewEntryWithAttributes",
				c("knownAttribute", create(WellKnownAttributes.COLOR.name()), create("red"))
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNotNull(cmd);
		ActorDescriptor ad = (ActorDescriptor) cmd;
		
		assertEquals(1, ad.attributes.size());
		assertEquals(WellKnownAttributes.COLOR, ((WellKnownEntityAttribute)ad.attributes.get(0)).attr);
		assertEquals("COLOR", ad.attributes.get(0).getName());
		assertEquals("red", ad.attributes.get(0).getValue());
	}
	

	@Test
	public void testEvalNoRet()
	{
		SaacContainer f = c
		(
			"doWhen",
			//not the current time +15 secounds modification
			c("isAfterDate", create(System.currentTimeMillis()+15000)),
			c
			(
				"funcNewEntryWithAttributes",
				c("knownAttribute", create(WellKnownAttributes.COLOR.name()), create("red"))
			)
		);
		
		SaacTestComponents saac = SaacTestTools.createSaacTestDefaultComponents();
		SaacEnv env = saac.compile(f);
		GetBy1<ModificationCommand, EvalContext> root = (GetBy1<ModificationCommand, EvalContext>) env.getRoot();
		
		EvalContext ctx = new EvalContext();
		ModificationCommand cmd = root.getBy(ctx);
		assertNull(cmd);
	}
}
