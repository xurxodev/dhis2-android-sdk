/*
 *  Copyright (c) 2004-2021, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.android.core.relationship.internal;

import org.hisp.dhis.android.core.arch.db.stores.internal.StoreWithState;
import org.hisp.dhis.android.core.arch.handlers.internal.HandleAction;
import org.hisp.dhis.android.core.arch.handlers.internal.Handler;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.data.relationship.RelationshipSamples;
import org.hisp.dhis.android.core.relationship.Relationship;
import org.hisp.dhis.android.core.relationship.RelationshipConstraintType;
import org.hisp.dhis.android.core.relationship.RelationshipHelper;
import org.hisp.dhis.android.core.relationship.RelationshipItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RelationshipHandlerShould extends RelationshipSamples {

    @Mock
    private RelationshipStore relationshipStore;

    @Mock
    private RelationshipItemStore relationshipItemStore;

    @Mock
    private Handler<RelationshipItem> relationshipItemHandler;

    @Mock
    private RelationshipItemElementStoreSelector storeSelector;

    @Mock
    private RelationshipDHISVersionManager versionManager;

    @Mock
    private StoreWithState itemElementStore;

    private final String NEW_UID = "new-uid";

    private final String TEI_3_UID = "tei3";
    private final String TEI_4_UID = "tei4";

    private final RelationshipItem tei3Item = RelationshipHelper.teiItem(TEI_3_UID);
    private final RelationshipItem tei4Item = RelationshipHelper.teiItem(TEI_4_UID);

    private final Relationship existingRelationship = get230();

    private final Relationship existingRelationshipWithNewUid = get230().toBuilder().uid(NEW_UID).build();

    private final Relationship newRelationship = get230(NEW_UID, TEI_3_UID, TEI_4_UID);


    // object to test
    private RelationshipHandlerImpl relationshipHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        relationshipHandler = new RelationshipHandlerImpl(relationshipStore, relationshipItemStore,
                relationshipItemHandler, storeSelector, versionManager);

        when(storeSelector.getElementStore(any(RelationshipItem.class))).thenReturn(itemElementStore);
        when(itemElementStore.exists(FROM_UID)).thenReturn(true);
        when(itemElementStore.exists(TO_UID)).thenReturn(true);
        when(itemElementStore.exists(TEI_3_UID)).thenReturn(true);
        when(itemElementStore.exists(TEI_4_UID)).thenReturn(true);
        when(relationshipItemStore.getRelationshipUidsForItems(fromItem, toItem)).thenReturn(Collections.singletonList(UID));
        when(relationshipItemStore.getRelationshipUidsForItems(tei3Item, tei4Item)).thenReturn(Collections.emptyList());
        when(relationshipStore.selectByUid(UID)).thenReturn(get230());
        when(versionManager.isRelationshipSupported(any(Relationship.class))).thenReturn(true);

        when(relationshipStore.updateOrInsert(any())).thenReturn(HandleAction.Insert);
    }

    @Test(expected = RuntimeException.class)
    public void throw_exception_when_relationship_is_no_compatible() {
        when(versionManager.isRelationshipSupported(existingRelationship)).thenReturn(false);
        relationshipHandler.handle(existingRelationship);
    }

    @Test()
    public void call_relationship_item_store_for_existing_relationship() {
        relationshipHandler.handle(existingRelationship);
        verify(relationshipItemStore).getRelationshipUidsForItems(fromItem, toItem);
    }

    @Test()
    public void call_relationship_store_for_existing_relationship() {
        relationshipHandler.handle(existingRelationship);
        verify(relationshipStore).selectByUid(UID);
    }

    @Test()
    public void not_delete_relationship_if_existing_id_matches() {
        relationshipHandler.handle(existingRelationship);
        verify(relationshipStore, never()).delete(UID);
    }

    @Test()
    public void not_call_delete_if_no_existing_relationship() {
        relationshipHandler.handle(newRelationship);
        verify(relationshipStore, never()).delete(UID);
    }

    @Test()
    public void delete_relationship_if_existing_id_doesnt_match() {
        relationshipHandler.handle(existingRelationshipWithNewUid);
        verify(relationshipStore).delete(UID);
    }

    @Test()
    public void update_relationship_store_for_existing_relationship() {
        relationshipHandler.handle(existingRelationship);
        verify(relationshipStore).updateOrInsert(existingRelationship);
    }

    @Test()
    public void update_relationship_store_for_existing_relationship_with_new_uid() {
        relationshipHandler.handle(existingRelationshipWithNewUid);
        verify(relationshipStore).updateOrInsert(existingRelationshipWithNewUid);
    }

    @Test()
    public void update_relationship_store_for_new_relationship() {
        relationshipHandler.handle(newRelationship);
        verify(relationshipStore).updateOrInsert(newRelationship);
    }

    @Test()
    public void update_relationship_handler_store_for_existing_relationship() {
        relationshipHandler.handle(existingRelationship);
        verify(relationshipItemHandler).handle(fromItem.toBuilder().relationship(ObjectWithUid.create(existingRelationship.uid()))
                .relationshipItemType(RelationshipConstraintType.FROM).build());
        verify(relationshipItemHandler).handle(toItem.toBuilder().relationship(ObjectWithUid.create(existingRelationship.uid()))
                .relationshipItemType(RelationshipConstraintType.TO).build());
    }

    @Test()
    public void update_relationship_item_handler_for_existing_relationship_with_new_uid() {
        relationshipHandler.handle(existingRelationshipWithNewUid);
        verify(relationshipItemHandler).handle(fromItem.toBuilder().relationship(ObjectWithUid.create(existingRelationshipWithNewUid.uid()))
                .relationshipItemType(RelationshipConstraintType.FROM).build());
        verify(relationshipItemHandler).handle(toItem.toBuilder().relationship(ObjectWithUid.create(existingRelationshipWithNewUid.uid()))
                .relationshipItemType(RelationshipConstraintType.TO).build());
    }

    @Test()
    public void update_relationship_item_handler_for_new_relationship() {
        relationshipHandler.handle(newRelationship);
        verify(relationshipItemHandler).handle(tei3Item.toBuilder().relationship(ObjectWithUid.create(newRelationship.uid()))
                .relationshipItemType(RelationshipConstraintType.FROM).build());
        verify(relationshipItemHandler).handle(tei4Item.toBuilder().relationship(ObjectWithUid.create(newRelationship.uid()))
                .relationshipItemType(RelationshipConstraintType.TO).build());
    }
}