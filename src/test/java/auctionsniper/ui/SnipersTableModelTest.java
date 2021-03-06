package auctionsniper.ui;


import auctionsniper.SniperSnapshot;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static auctionsniper.ui.SnipersTableModel.textFor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

@RunWith(JMock.class)
public class SnipersTableModelTest {
    private final Mockery context = new Mockery();
    private TableModelListener listener = context.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();
    @Before
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }
    @Test
    public void
    hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(Column.values().length));
    }

    @Test public void
    setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item id");
        SniperSnapshot bidding = joining.bidding(555, 666);
        context.checking(new Expectations() {{
            allowing(listener).tableChanged(with(anyInsertionEvent()));
            one(listener).tableChanged(with(aChangeInRow(0)));
        }});
//        model.addSniper(joining);
        model.sniperStateChanged(bidding);
        assertRowMatchesSnapshot(0, bidding);
    }

    @Test public void
    setsUpColumnHeadings() {
        for (Column column: Column.values()) {
            assertEquals(column.name, model.getColumnName(column.ordinal()));
        }
    }

    @Test public void
    notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        context.checking(new Expectations() { {
            one(listener).tableChanged(with(anInsertionAtRow(0)));
        }});
        assertEquals(0, model.getRowCount());
//        model.addSniper(joining);
        assertEquals(1, model.getRowCount());
        assertRowMatchesSnapshot(0, joining);
    }

    private Matcher<TableModelEvent> anInsertionAtRow(final int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT));
    }

    private Matcher<TableModelEvent> anyInsertionEvent() {
        return hasProperty("type", equalTo(TableModelEvent.INSERT));
    }
    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals(snapshot.itemId, getValueAt(row, Column.ITEM_IDENTIFIER));
        assertEquals(snapshot.lastBid, getValueAt(row, Column.LAST_BID));
        assertEquals(snapshot.lastPrice, getValueAt(row, Column.LAST_PRICE));
        assertEquals(textFor(snapshot.state), getValueAt(row, Column.SNIPER_STATE));
    }

    private Object getValueAt(final int rowIndex, Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }



    @Test public void
    holdsSnipersInAdditionOrder() {
        context.checking(new Expectations() { {
            ignoring(listener);
        }});
//        model.addSniper(SniperSnapshot.joining("item 0"));
//        model.addSniper(SniperSnapshot.joining("item 1"));
        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
    }

    private String cellValue(int i, Column itemIdentifier) {
        return (String)model.getValueAt(i, itemIdentifier.ordinal());
    }


    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }
    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }

    private Matcher<TableModelEvent> samePropertyValuesAs(TableModelEvent tableModelEvent) {

        return new FeatureMatcher<TableModelEvent, Integer>(
                equalTo(tableModelEvent.getFirstRow()), "sniper that is ", "was")
        {
            @Override
            protected Integer featureValueOf(TableModelEvent tableModelEvent) {
                return tableModelEvent.getFirstRow();
            }
        };
    }

    private Matcher<TableModelEvent> aChangeInRow(final int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS,
                TableModelEvent.UPDATE));
    }


}