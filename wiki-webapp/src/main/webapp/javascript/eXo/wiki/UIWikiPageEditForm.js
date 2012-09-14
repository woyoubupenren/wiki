/**
 * Copyright (C) 2010 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */


if (!eXo.wiki)
  eXo.wiki = {};

function UIWikiPageEditForm() {
};

UIWikiPageEditForm.prototype.editPageContent = function(pageEditFormId) {
  var pageEditForm = document.getElementById(pageEditFormId);
  var titleContainer = $(pageEditForm).find('div.UIWikiPageTitleControlForm_PageEditForm')[0];
  var titleInput = $(titleContainer).find('input')[0];
  eXo.wiki.UIWikiPageEditForm.changed = false;

  $(titleInput).change(function() {
    eXo.wiki.UIWikiPageEditForm.changed = true;
    $(titleInput).change(null);
  });

  var textAreaContainer = $(pageEditForm).find('div.UIWikiPageContentInputContainer')[0];
  if (textAreaContainer != null) {
    var textArea = $(textAreaContainer).find('textarea')[0];
    $(textArea).change(function() {
      eXo.wiki.UIWikiPageEditForm.changed = true;
      $(textArea).change(null);
    });
  } else {
    eXo.wiki.UIWikiPageEditForm.changed = true;
  }
};

UIWikiPageEditForm.prototype.cancel = function(uicomponentId, titleMessage, message, submitClass, submitLabel, cancelLabel){
  if (eXo.wiki.UIWikiPageEditForm.changed == true) {
    eXo.wiki.UIConfirmBox.render(uicomponentId, titleMessage, message, submitClass, submitLabel, cancelLabel);
    return false;
  }
  return true;
};

eXo.wiki.UIWikiPageEditForm = new UIWikiPageEditForm();
_module.UIWikiPageEditForm = eXo.wiki.UIWikiPageEditForm;
