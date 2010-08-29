<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/webed" prefix="webed" %>
<%@ taglib uri="http://psi.ontopia.net/jsp/taglib/tolog" prefix="tolog" %>
<tolog:context topicmap="test.ltm">
  <webed:form actiongroup="testActionGroup">
  </webed:form>
  <webed:form actiongroup="testActionGroup">
  <tolog:set var="pattern">foo|bar</tolog:set>
    <webed:field type="textField" action="dummy" id="field1" pattern="pattern">VALUE</webed:field>
    <webed:field type="textField" action="dummy" id="field2">VALUE</webed:field>
    <webed:link href="/webedtest/test/FieldTag/testValidation.jsp">
      Validate.
    </webed:link>
  </webed:form>
  <webed:form actiongroup="testActionGroup">
  </webed:form>
</tolog:context>
