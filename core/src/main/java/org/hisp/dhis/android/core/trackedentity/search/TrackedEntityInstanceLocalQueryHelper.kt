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
package org.hisp.dhis.android.core.trackedentity.search

import dagger.Reusable
import java.util.*
import javax.inject.Inject
import org.hisp.dhis.android.core.arch.db.querybuilders.internal.WhereClauseBuilder
import org.hisp.dhis.android.core.arch.helpers.CollectionsHelper
import org.hisp.dhis.android.core.arch.helpers.DateUtils
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope.OrderByDirection
import org.hisp.dhis.android.core.arch.repositories.scope.internal.FilterItemOperator
import org.hisp.dhis.android.core.arch.repositories.scope.internal.RepositoryScopeFilterItem
import org.hisp.dhis.android.core.common.*
import org.hisp.dhis.android.core.enrollment.EnrollmentTableInfo
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.event.EventTableInfo
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitMode
import org.hisp.dhis.android.core.organisationunit.OrganisationUnitTableInfo
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValueTableInfo
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceTableInfo
import org.hisp.dhis.android.core.user.AuthenticatedUserTableInfo
import org.hisp.dhis.android.core.user.UserOrganisationUnitLinkTableInfo

@Reusable
@Suppress("TooManyFunctions")
internal class TrackedEntityInstanceLocalQueryHelper @Inject constructor(
    private val dateFilterPeriodHelper: DateFilterPeriodHelper
) {
    private val teiAlias = "tei"
    private val enrollmentAlias = "en"
    private val eventAlias = "ev"
    private val orgunitAlias = "ou"
    private val teavAlias = "teav"

    private val teiUid = dot(teiAlias, "uid")
    private val teiAll = dot(teiAlias, "*")
    private val teiLastUpdated = dot(teiAlias, "lastUpdated")
    private val enrollmentDate = EnrollmentTableInfo.Columns.ENROLLMENT_DATE
    private val program = EnrollmentTableInfo.Columns.PROGRAM
    private val trackedEntityAttribute = TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_ATTRIBUTE
    private val trackedEntityInstance = TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_INSTANCE

    fun getSqlQuery(scope: TrackedEntityInstanceQueryRepositoryScope, excludeList: Set<String>, limit: Int): String {
        return getSqlQuery(scope, excludeList, limit, teiAll)
    }

    fun getUidsWhereClause(
        scope: TrackedEntityInstanceQueryRepositoryScope,
        excludeList: Set<String>,
        limit: Int
    ): String {
        val selectSubQuery = getSqlQuery(scope, excludeList, limit, teiUid)
        return WhereClauseBuilder()
            .appendInSubQuery(IdentifiableColumns.UID, selectSubQuery)
            .build()
    }

    private fun getSqlQuery(
        scope: TrackedEntityInstanceQueryRepositoryScope,
        excludeList: Set<String>,
        limit: Int,
        columns: String
    ): String {
        var queryStr = "SELECT DISTINCT $columns FROM ${TrackedEntityInstanceTableInfo.TABLE_INFO.name()} $teiAlias"

        val where = WhereClauseBuilder()
        if (hasProgram(scope)) {
            queryStr += " JOIN ${EnrollmentTableInfo.TABLE_INFO.name()} $enrollmentAlias"
            queryStr += " ON ${dot(teiAlias, IdentifiableColumns.UID)} = " +
                dot(enrollmentAlias, EnrollmentTableInfo.Columns.TRACKED_ENTITY_INSTANCE)

            appendProgramWhere(where, scope)
            if (hasEvent(scope)) {
                queryStr += String.format(
                    " JOIN %s %s ON %s = %s",
                    EventTableInfo.TABLE_INFO.name(), eventAlias,
                    dot(enrollmentAlias, IdentifiableColumns.UID),
                    dot(eventAlias, EventTableInfo.Columns.ENROLLMENT)
                )
                appendEventWhere(where, scope)
            }
        }

        if (hasOrgunits(scope)) {
            queryStr += String.format(
                " JOIN %s %s ON %s = %s",
                OrganisationUnitTableInfo.TABLE_INFO.name(), orgunitAlias,
                dot(teiAlias, TrackedEntityInstanceTableInfo.Columns.ORGANISATION_UNIT),
                dot(orgunitAlias, IdentifiableColumns.UID)
            )
            appendOrgunitWhere(where, scope)
        }

        if (scope.trackedEntityType() != null) {
            where.appendKeyStringValue(
                dot(teiAlias, TrackedEntityInstanceTableInfo.Columns.TRACKED_ENTITY_TYPE),
                escapeQuotes(scope.trackedEntityType())
            )
        }

        if (scope.states() == null) {
            where.appendNotKeyStringValue(dot(teiAlias, DataColumns.STATE), State.RELATIONSHIP.name)
        } else {
            where.appendInKeyEnumValues(dot(teiAlias, DataColumns.STATE), scope.states())
        }

        if (!scope.includeDeleted()) {
            where.appendKeyOperatorValue(dot(teiAlias, TrackedEntityInstanceTableInfo.Columns.DELETED), "!=", "1")
        }

        appendQueryWhere(where, scope)
        appendFiltersWhere(where, scope)
        appendExcludeList(where, excludeList)

        if (!where.isEmpty) {
            queryStr += " WHERE " + where.build()
        }
        queryStr += orderByClause(scope)
        if (limit > 0) {
            queryStr += " LIMIT $limit"
        }

        return queryStr
    }

    private fun hasProgram(scope: TrackedEntityInstanceQueryRepositoryScope): Boolean {
        return scope.program() != null
    }

    private fun hasEvent(scope: TrackedEntityInstanceQueryRepositoryScope): Boolean {
        return scope.eventFilters().isNotEmpty()
    }

    private fun appendProgramWhere(where: WhereClauseBuilder, scope: TrackedEntityInstanceQueryRepositoryScope) {
        if (scope.program() != null) {
            where.appendKeyStringValue(dot(enrollmentAlias, program), escapeQuotes(scope.program()))
        }
        if (scope.programDate() != null) {
            val enrollmentDateStr = "date(${dot(enrollmentAlias, enrollmentDate)})"

            dateFilterPeriodHelper.getStartDate(scope.programDate()!!)?.let { startDate ->
                val startDateStr = DateUtils.SIMPLE_DATE_FORMAT.format(startDate)
                where.appendKeyGreaterOrEqStringValue(enrollmentDateStr, startDateStr)
            }

            dateFilterPeriodHelper.getEndDate(scope.programDate()!!)?.let { endDate ->
                val endDateStr = DateUtils.SIMPLE_DATE_FORMAT.format(endDate)
                where.appendKeyLessThanOrEqStringValue(enrollmentDateStr, endDateStr)
            }
        }
        if (scope.enrollmentStatus() != null) {
            where.appendInKeyEnumValues(
                dot(enrollmentAlias, EnrollmentTableInfo.Columns.STATUS),
                scope.enrollmentStatus()
            )
        }
        if (!scope.includeDeleted()) {
            where.appendKeyOperatorValue(dot(enrollmentAlias, EnrollmentTableInfo.Columns.DELETED), "!=", "1")
        }
        if (scope.followUp() != null) {
            val value = if (scope.followUp() == true) 1 else 0
            where.appendKeyNumberValue(dot(enrollmentAlias, EnrollmentTableInfo.Columns.FOLLOW_UP), value)
        }
    }

    private fun hasOrgunits(scope: TrackedEntityInstanceQueryRepositoryScope): Boolean {
        return (
            (
                scope.orgUnits().isNotEmpty() &&
                    OrganisationUnitMode.ALL != scope.orgUnitMode() &&
                    OrganisationUnitMode.ACCESSIBLE != scope.orgUnitMode()
                ) ||
                OrganisationUnitMode.CAPTURE == scope.orgUnitMode() ||
                hasOrgunitSortOrder(scope)
            )
    }

    private fun hasOrgunitSortOrder(scope: TrackedEntityInstanceQueryRepositoryScope): Boolean {
        return scope.order().any { it.column() == TrackedEntityInstanceQueryScopeOrderColumn.ORGUNIT_NAME }
    }

    private fun appendOrgunitWhere(where: WhereClauseBuilder, scope: TrackedEntityInstanceQueryRepositoryScope) {
        val ouMode = scope.orgUnitMode() ?: OrganisationUnitMode.SELECTED
        val inner = WhereClauseBuilder()
        when (ouMode) {
            OrganisationUnitMode.DESCENDANTS -> scope.orgUnits().forEach { orgUnit ->
                inner.appendOrKeyLikeStringValue(
                    dot(orgunitAlias, OrganisationUnitTableInfo.Columns.PATH),
                    "%" + escapeQuotes(orgUnit) + "%"
                )
            }
            OrganisationUnitMode.CHILDREN -> scope.orgUnits().forEach { orgUnit ->
                inner.appendOrKeyStringValue(
                    dot(orgunitAlias, OrganisationUnitTableInfo.Columns.PARENT), escapeQuotes(orgUnit)
                )

                // TODO Include orgunit?
                inner.appendOrKeyStringValue(dot(orgunitAlias, IdentifiableColumns.UID), escapeQuotes(orgUnit))
            }
            OrganisationUnitMode.CAPTURE ->
                inner.appendComplexQuery(
                    String.format(
                        "%s IN (SELECT %s FROM %s WHERE %s = '%s')",
                        dot(orgunitAlias, IdentifiableColumns.UID),
                        UserOrganisationUnitLinkTableInfo.Columns.ORGANISATION_UNIT,
                        UserOrganisationUnitLinkTableInfo.TABLE_INFO.name(),
                        UserOrganisationUnitLinkTableInfo.Columns.ORGANISATION_UNIT_SCOPE,
                        OrganisationUnit.Scope.SCOPE_DATA_CAPTURE.name
                    )
                )
            OrganisationUnitMode.SELECTED -> scope.orgUnits().forEach { orgUnit ->
                inner.appendOrKeyStringValue(dot(orgunitAlias, IdentifiableColumns.UID), escapeQuotes(orgUnit))
            }
            OrganisationUnitMode.ACCESSIBLE, OrganisationUnitMode.ALL -> {}
        }
        if (!inner.isEmpty) {
            where.appendComplexQuery(inner.build())
        }
    }

    private fun appendQueryWhere(where: WhereClauseBuilder, scope: TrackedEntityInstanceQueryRepositoryScope) {
        scope.query()?.let { query ->
            val tokens = query.value().split(" ".toRegex()).toTypedArray()
            for (token in tokens) {
                val valueStr =
                    if (query.operator() == FilterItemOperator.LIKE) "%${escapeQuotes(token)}%"
                    else escapeQuotes(token)

                val sub = String.format(
                    "SELECT 1 FROM %s %s WHERE %s = %s AND %s %s '%s'",
                    TrackedEntityAttributeValueTableInfo.TABLE_INFO.name(), teavAlias,
                    dot(teavAlias, trackedEntityInstance), dot(teiAlias, IdentifiableColumns.UID),
                    dot(teavAlias, TrackedEntityAttributeValueTableInfo.Columns.VALUE),
                    query.operator().sqlOperator, valueStr
                )

                where.appendExistsSubQuery(sub)
            }
        }
    }

    private fun appendFiltersWhere(where: WhereClauseBuilder, scope: TrackedEntityInstanceQueryRepositoryScope) {
        // TODO Filter by program attributes in case program is provided
        appendFilterWhere(where, scope.filter())
        appendFilterWhere(where, scope.attribute())
    }

    private fun appendFilterWhere(where: WhereClauseBuilder, items: List<RepositoryScopeFilterItem>) {
        for (item in items) {
            val valueStr =
                if (item.operator() == FilterItemOperator.LIKE) "%${escapeQuotes(item.value())}%"
                else escapeQuotes(item.value())

            val sub = String.format(
                "SELECT 1 FROM %s %s WHERE %s = %s AND %s = '%s' AND %s %s '%s'",
                TrackedEntityAttributeValueTableInfo.TABLE_INFO.name(), teavAlias,
                dot(teavAlias, trackedEntityInstance), dot(teiAlias, IdentifiableColumns.UID),
                dot(teavAlias, trackedEntityAttribute), escapeQuotes(item.key()),
                dot(teavAlias, TrackedEntityAttributeValueTableInfo.Columns.VALUE),
                item.operator().sqlOperator, valueStr
            )

            where.appendExistsSubQuery(sub)
        }
    }

    private fun appendExcludeList(where: WhereClauseBuilder, excludeList: Set<String>?) {
        if (!excludeList.isNullOrEmpty()) {
            where.appendNotInKeyStringValues(teiUid, excludeList.toList())
        }
    }

    private fun appendEventWhere(where: WhereClauseBuilder, scope: TrackedEntityInstanceQueryRepositoryScope) {
        val innerClause = WhereClauseBuilder()
        scope.eventFilters().forEach { eventFilter ->
            getEventFilterClause(eventFilter)?.let { eventFilterClause ->
                innerClause.appendOrComplexQuery(eventFilterClause)
            }
        }
        if (!innerClause.isEmpty) {
            where.appendComplexQuery(innerClause.build())
            where.appendKeyOperatorValue(dot(eventAlias, EventTableInfo.Columns.DELETED), "!=", "1")
        }
    }

    private fun getEventFilterClause(eventFilter: TrackedEntityInstanceQueryEventFilter): String? {
        val innerClause = WhereClauseBuilder()

        eventFilter.assignedUserMode()?.let { mode -> appendAssignedUserMode(innerClause, mode) }

        val statusList = eventFilter.eventStatus()

        if (statusList == null) {
            appendEventDates(innerClause, eventFilter, EventTableInfo.Columns.EVENT_DATE)
        } else if (statusList.size > 0 && eventFilter.eventDate() != null) {
            val nowStr = DateUtils.SIMPLE_DATE_FORMAT.format(Date())
            val statusListWhere = WhereClauseBuilder()
            for (eventStatus in statusList) {
                val statusWhere = WhereClauseBuilder()
                when (eventStatus) {
                    EventStatus.ACTIVE -> {
                        appendEventDates(statusWhere, eventFilter, EventTableInfo.Columns.EVENT_DATE)
                        statusWhere.appendInKeyEnumValues(
                            dot(eventAlias, EventTableInfo.Columns.STATUS),
                            listOf(EventStatus.ACTIVE, EventStatus.SCHEDULE, EventStatus.OVERDUE)
                        )
                    }
                    EventStatus.COMPLETED, EventStatus.VISITED -> {
                        appendEventDates(statusWhere, eventFilter, EventTableInfo.Columns.EVENT_DATE)
                        statusWhere.appendKeyStringValue(dot(eventAlias, EventTableInfo.Columns.STATUS), eventStatus)
                    }
                    EventStatus.SCHEDULE -> {
                        appendEventDates(statusWhere, eventFilter, EventTableInfo.Columns.DUE_DATE)
                        statusWhere.appendIsNullValue(EventTableInfo.Columns.EVENT_DATE)
                        statusWhere.appendInKeyEnumValues(
                            dot(eventAlias, EventTableInfo.Columns.STATUS),
                            listOf(EventStatus.SCHEDULE, EventStatus.OVERDUE)
                        )
                        statusWhere.appendKeyGreaterOrEqStringValue(
                            "date(${dot(eventAlias, EventTableInfo.Columns.DUE_DATE)})", nowStr
                        )
                    }
                    EventStatus.OVERDUE -> {
                        appendEventDates(statusWhere, eventFilter, EventTableInfo.Columns.DUE_DATE)
                        statusWhere.appendIsNullValue(EventTableInfo.Columns.EVENT_DATE)
                        statusWhere.appendInKeyEnumValues(
                            dot(eventAlias, EventTableInfo.Columns.STATUS),
                            listOf(EventStatus.SCHEDULE, EventStatus.OVERDUE)
                        )
                        statusWhere.appendKeyLessThanStringValue(
                            "date(${dot(eventAlias, EventTableInfo.Columns.DUE_DATE)})", nowStr
                        )
                    }
                    EventStatus.SKIPPED -> {
                        statusWhere.appendKeyStringValue(dot(eventAlias, EventTableInfo.Columns.STATUS), eventStatus)
                        appendEventDates(statusWhere, eventFilter, EventTableInfo.Columns.DUE_DATE)
                    }
                    else -> {
                    }
                }
                statusListWhere.appendOrComplexQuery(statusWhere.build())
            }
            innerClause.appendComplexQuery(statusListWhere.build())
        }
        return if (innerClause.isEmpty) null else innerClause.build()
    }

    private fun appendEventDates(
        where: WhereClauseBuilder,
        eventFilter: TrackedEntityInstanceQueryEventFilter,
        refDate: String
    ) {
        if (eventFilter.eventDate() != null) {
            val refDateStr = "date(${dot(eventAlias, refDate)})"
            dateFilterPeriodHelper.getStartDate(eventFilter.eventDate()!!)?.let { startDate ->
                val startDateStr = DateUtils.SIMPLE_DATE_FORMAT.format(startDate)
                where.appendKeyGreaterOrEqStringValue(refDateStr, startDateStr)
            }
            dateFilterPeriodHelper.getEndDate(eventFilter.eventDate()!!)?.let { endDate ->
                val endDateStr = DateUtils.SIMPLE_DATE_FORMAT.format(endDate)
                where.appendKeyLessThanOrEqStringValue(refDateStr, endDateStr)
            }
        }
    }

    private fun appendAssignedUserMode(
        where: WhereClauseBuilder,
        mode: AssignedUserMode
    ) {
        val assignedUserColumn = dot(eventAlias, EventTableInfo.Columns.ASSIGNED_USER)
        when (mode) {
            AssignedUserMode.CURRENT -> {
                val subquery = String.format(
                    "(SELECT %s FROM %s LIMIT 1)",
                    AuthenticatedUserTableInfo.Columns.USER,
                    AuthenticatedUserTableInfo.TABLE_INFO.name()
                )
                where.appendKeyOperatorValue(assignedUserColumn, "IN", subquery)
            }
            AssignedUserMode.ANY -> where.appendIsNotNullValue(assignedUserColumn)
            AssignedUserMode.NONE -> where.appendIsNullValue(assignedUserColumn)
            else -> {
            }
        }
    }

    private fun orderByClause(scope: TrackedEntityInstanceQueryRepositoryScope): String {
        val orderClauses = scope.order().mapNotNull { item ->
            when (item.column().type()) {
                TrackedEntityInstanceQueryScopeOrderColumn.Type.CREATED ->
                    if (hasProgram(scope)) {
                        orderByEnrollmentField(scope.program(), IdentifiableColumns.CREATED, item.direction())
                    } else {
                        dot(teiAlias, IdentifiableColumns.CREATED) + " " + item.direction().name
                    }

                TrackedEntityInstanceQueryScopeOrderColumn.Type.LAST_UPDATED ->
                    if (hasProgram(scope)) {
                        orderByEnrollmentField(scope.program(), IdentifiableColumns.LAST_UPDATED, item.direction())
                    } else {
                        dot(teiAlias, IdentifiableColumns.LAST_UPDATED) + " " + item.direction().name
                    }

                TrackedEntityInstanceQueryScopeOrderColumn.Type.ORGUNIT_NAME ->
                    dot(orgunitAlias, IdentifiableColumns.NAME) + " " + item.direction().name

                TrackedEntityInstanceQueryScopeOrderColumn.Type.ATTRIBUTE ->
                    orderByAttribute(item)

                TrackedEntityInstanceQueryScopeOrderColumn.Type.ENROLLMENT_DATE ->
                    orderByEnrollmentField(scope.program(), enrollmentDate, item.direction())

                TrackedEntityInstanceQueryScopeOrderColumn.Type.INCIDENT_DATE ->
                    orderByEnrollmentField(
                        scope.program(), EnrollmentTableInfo.Columns.INCIDENT_DATE, item.direction()
                    )

                TrackedEntityInstanceQueryScopeOrderColumn.Type.ENROLLMENT_STATUS ->
                    orderByEnrollmentField(scope.program(), EnrollmentTableInfo.Columns.STATUS, item.direction())

                TrackedEntityInstanceQueryScopeOrderColumn.Type.EVENT_DATE -> {
                    val eventField = "IFNULL(${EventTableInfo.Columns.EVENT_DATE},${EventTableInfo.Columns.DUE_DATE})"
                    orderByEventField(scope.program(), eventField, item.direction())
                }

                TrackedEntityInstanceQueryScopeOrderColumn.Type.COMPLETION_DATE ->
                    orderByEventField(scope.program(), EventTableInfo.Columns.COMPLETE_DATE, item.direction())

                else -> null
            }
        } + orderByLastUpdated

        return " ORDER BY ${orderClauses.joinToString(", ")}"
    }

    // TODO In case a program uid is provided, the server orders by enrollmentStatus.
    private val orderByLastUpdated: String
        get() = "$teiLastUpdated DESC "

    private fun orderByEnrollmentField(program: String?, field: String, dir: OrderByDirection): String {
        val programClause = if (program == null) "" else "AND ${EnrollmentTableInfo.Columns.PROGRAM} = '$program'"
        return String.format(
            "IFNULL((SELECT %s FROM %s WHERE %s = %s %s ORDER BY %s DESC LIMIT 1), 'zzzzz') %s",
            field,
            EnrollmentTableInfo.TABLE_INFO.name(),
            EnrollmentTableInfo.Columns.TRACKED_ENTITY_INSTANCE,
            dot(teiAlias, IdentifiableColumns.UID),
            programClause,
            EnrollmentTableInfo.Columns.ENROLLMENT_DATE,
            dir.name
        )
    }

    private fun orderByEventField(program: String?, field: String, dir: OrderByDirection): String {
        val programClause = if (program == null) "" else "AND ${EnrollmentTableInfo.Columns.PROGRAM} = '$program'"
        return String.format(
            "(SELECT %s FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = %s %s) " +
                "ORDER BY IFNULL(%s, %s) DESC LIMIT 1) %s",
            field,
            EventTableInfo.TABLE_INFO.name(),
            EventTableInfo.Columns.ENROLLMENT,
            IdentifiableColumns.UID,
            EnrollmentTableInfo.TABLE_INFO.name(),
            EnrollmentTableInfo.Columns.TRACKED_ENTITY_INSTANCE,
            dot(teiAlias, IdentifiableColumns.UID),
            programClause,
            EventTableInfo.Columns.EVENT_DATE, EventTableInfo.Columns.DUE_DATE,
            dir.name
        )
    }

    private fun orderByAttribute(item: TrackedEntityInstanceQueryScopeOrderByItem): String {
        // Trick to put null values at the end of the list
        val attOrder = String.format(
            "IFNULL((SELECT %s FROM %s WHERE %s = %s AND %s = %s), 'zzzzzzzz')",
            TrackedEntityAttributeValueTableInfo.Columns.VALUE,
            TrackedEntityAttributeValueTableInfo.TABLE_INFO.name(),
            TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_ATTRIBUTE,
            CollectionsHelper.withSingleQuotationMarks(item.column().value()),
            TrackedEntityAttributeValueTableInfo.Columns.TRACKED_ENTITY_INSTANCE,
            dot(teiAlias, IdentifiableColumns.UID)
        )
        return "$attOrder ${item.direction().name}"
    }

    private fun dot(item1: String, item2: String): String {
        return "$item1.$item2"
    }

    private fun escapeQuotes(value: String?): String {
        return value!!.replace("'".toRegex(), "''")
    }
}
