package believe.xml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;

public class XMLIntegerTest {
  private XMLInteger primitive;

  @Mock private XMLElement element;
  @Mock private XMLElementList list;

  @Before
  public void setUp() {
    initMocks(this);
    primitive = new XMLInteger();
  }

  @Test(expected=XMLLoadingException.class)
  public void fillNodeShouldThrowXMLLoadingExceptionWhenThereAreChildren() throws XMLLoadingException {
    when(element.getName()).thenReturn("haha");
    when(element.getChildren()).thenReturn(list);
    when(list.size()).thenReturn(2);
    primitive.fillNode(element);
  }

  @Test(expected=NumberFormatException.class)
  public void fillNodeShouldThrowExceptionWhenValueIsntInt() throws XMLLoadingException {
    final String name = "anode";
    final String stringValue = "123asna!@$";

    when(element.getChildren()).thenReturn(list);
    when(list.size()).thenReturn(0);
    when(element.getName()).thenReturn(name);
    when(element.getContent()).thenReturn(stringValue);

    primitive.fillNode(element);
  }

  @Test
  public void fillNodeShouldPopulateNameAndValue() throws XMLLoadingException {
    final String name = "anode";
    final String stringValue = "654";
    final int value = 654;

    when(element.getChildren()).thenReturn(list);
    when(list.size()).thenReturn(0);
    when(element.getName()).thenReturn(name);
    when(element.getContent()).thenReturn(stringValue);

    primitive.fillNode(element);
    assertThat(primitive.name, is(name));
    assertThat(primitive.value, is(value));
  }

}
