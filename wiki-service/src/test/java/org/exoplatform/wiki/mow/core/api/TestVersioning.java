/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.wiki.mow.core.api;

import java.util.Iterator;
import java.util.List;

import org.exoplatform.wiki.chromattic.ext.ntdef.NTFrozenNode;
import org.exoplatform.wiki.chromattic.ext.ntdef.NTVersion;
import org.exoplatform.wiki.mow.api.*;
import org.exoplatform.wiki.mow.core.api.wiki.WikiNodeType;
import org.exoplatform.wiki.mow.core.api.wiki.PageImpl;
import org.exoplatform.wiki.service.WikiService;

public class TestVersioning extends AbstractMOWTestcase {

  private WikiService wikiService;

  public void setUp() throws Exception {
    super.setUp();
    wikiService = container.getComponentInstanceOfType(WikiService.class);
  }

  public void testGetVersionHistory() throws Exception {
    Wiki wiki = wikiService.createWiki(WikiType.PORTAL.toString(), "versioning");
    Page page = new Page("testGetVersionHistory-001", "testGetVersionHistory-001");
    page = wikiService.createPage(wiki, "WikiHome", page);
    wikiService.createVersionOfPage(page);

    page = wikiService.getPageOfWikiByName(wiki.getType(), wiki.getOwner(), "testGetVersionHistory-001");
    assertNotNull(page);
    List<PageVersion> versions = wikiService.getVersionsOfPage(page);
    assertNotNull(versions);
    assertEquals(2, versions.size());
  }

  public void testCreateVersionHistoryTree() throws Exception {
    Wiki wiki = wikiService.createWiki(WikiType.PORTAL.toString(), "versioning");
    Page page = new Page("testCreateVersionHistoryTree-001", "testCreateVersionHistoryTree-001");
    page.setContent("testCreateVersionHistoryTree-ver0.0");
    page = wikiService.createPage(wiki, "WikiHome", page);

    page.setTitle("testCreateVersionHistoryTree");
    page.setContent("testCreateVersionHistoryTree-ver1.0");
    wikiService.updatePage(page);
    wikiService.createVersionOfPage(page);

    page.setContent("testCreateVersionHistoryTree-ver2.0");
    wikiService.updatePage(page);
    wikiService.createVersionOfPage(page);

    List<PageVersion> versions = wikiService.getVersionsOfPage(page);
    assertNotNull(versions);
    assertEquals(3, versions.size());

    // restore to previous version (testCreateVersionHistoryTree-ver1.0)
    wikiService.restoreVersionOfPage(versions.get(1).getName(), page);
    page = wikiService.getPageOfWikiByName(wiki.getType(), wiki.getOwner(), page.getName());
    assertEquals("testCreateVersionHistoryTree-ver1.0", page.getContent());

    page.setContent("testCreateVersionHistoryTree-ver3.0");
    wikiService.updatePage(page);
    wikiService.createVersionOfPage(page);

    versions = wikiService.getVersionsOfPage(page);
    assertNotNull(versions);
    assertEquals(5, versions.size());

    Iterator<PageVersion> itVersions = versions.iterator();
    PageVersion pageVersion = itVersions.next();
    assertEquals("testCreateVersionHistoryTree-ver3.0", pageVersion.getContent());

    pageVersion = itVersions.next();
    assertEquals("testCreateVersionHistoryTree-ver1.0", pageVersion.getContent());

    pageVersion = itVersions.next();
    assertEquals("testCreateVersionHistoryTree-ver2.0", pageVersion.getContent());

    pageVersion = itVersions.next();
    assertEquals("testCreateVersionHistoryTree-ver1.0", pageVersion.getContent());

    pageVersion = itVersions.next();
    assertEquals("testCreateVersionHistoryTree-ver0.0", pageVersion.getContent());
  }
}
