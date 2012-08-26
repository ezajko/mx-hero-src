package org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.Item;
import org.mxhero.engine.plugin.boxserver.dataaccess.rest.connector.domain.ItemCollection;

import com.google.common.collect.Lists;

public class ItemCollectionTest {
	
	ItemCollection target;

	@Before
	public void setUp() throws Exception {
		target = new ItemCollection();
	}

	@Test
	public void test_success() {
		Item item = new Item();
		item.setName("mxHero");
		target.setEntries(Lists.newArrayList(item));
		Item mxHeroFolder = target.getMxHeroFolder();
		assertNotNull(mxHeroFolder);
	}

	@Test
	public void test_success_2() {
		Item item = new Item();
		item.setName("mxhero");
		ArrayList<Item> newArrayList = Lists.newArrayList(item);
		for (int i = 0; i < 100; i++) {
			Item e = new Item();
			e.setName("algo"+i);
			newArrayList.add(e);
			
		}
		target.setEntries(newArrayList);
		Item mxHeroFolder = target.getMxHeroFolder();
		assertNotNull(mxHeroFolder);
	}

	@Test
	public void test_has_no_mxhero() {
		Item item = new Item();
		item.setName("mxhero443");
		ArrayList<Item> newArrayList = Lists.newArrayList(item);
		for (int i = 0; i < 100; i++) {
			Item e = new Item();
			e.setName("algo"+i);
			newArrayList.add(e);
			
		}
		target.setEntries(newArrayList);
		Item mxHeroFolder = target.getMxHeroFolder();
		assertNull(mxHeroFolder);
	}


	@Test
	public void test_has_no_items() {
		Item mxHeroFolder = target.getMxHeroFolder();
		assertNull(mxHeroFolder);
	}


	@Test
	public void test_has_no_items_empty() {
		target.setEntries(new ArrayList<Item>());
		Item mxHeroFolder = target.getMxHeroFolder();
		assertNull(mxHeroFolder);
	}
}
