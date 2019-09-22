package apidiff.internal.analysis.description;

import apidiff.internal.util.UtilTools;
import org.apache.commons.lang3.StringEscapeUtils;

public class TemplateDescription {
	
	protected String messageRemoveTemplate(final String typeStruture, final String nameStruture, final String path){
		String message = "";
		message += "<br><code>" + StringEscapeUtils.escapeHtml4(path) + "</code>: removed";
		message += "<br>" + typeStruture + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>";
		return message;
	}

	protected String messageVisibilityTemplate(final String typeStruture, final String nameStruture, final String typePath, final String path,  final String visibility1, final String visibility2){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += "<br> " + typeStruture + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br> changed visibility from <code>" +StringEscapeUtils.escapeHtml4(visibility1 ) + "</code> to <code>"  + StringEscapeUtils.escapeHtml4(visibility2) + "</code>";
		message += "<br>";
		return message;
	}
	
	protected String messageChangeDefaultValueTemplate(final String typeStruture, final String nameStruture, final String typePath, final String path,  final String value1, final String value2){
		String message = "";
		message += "<br>" + typePath + " <code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += "<b>Category Default Value:</b>";
		message += "<br>" + UtilTools.downCaseFirstLetter(typeStruture) + "<code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>changed default value from " + value1  + " to "  + value2;
		message += "<br>";
		return message;
	}

	protected String messageFinalTemplate(final String typeStruture, final String nameStruture, final String typePath, final String path, final Boolean gain){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += UtilTools.downCaseFirstLetter(typeStruture) + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += gain ? "<br>received the modifier <code>final</code>" : "<br>lost the modifier <code>final</code>";
		message += "<br>";
		return message;
	}
	
	protected String messageStaticTemplate(final String typeStruture, final String nameStruture, final String typePath, final String path, final Boolean gain){
		String message = "";
		message += "<br>" + typePath + " <code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += UtilTools.downCaseFirstLetter(typeStruture) + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += gain ? "<br>received the modifier <code>static</code>" : "<br>lost the modifier <code>static</code>";
		message += "<br>";
		return message;
	}
	
	protected String messageReturnTypeTemplate(final String typeStruture,  final String nameStruture, final String typePath, final String path){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += "<br>" + UtilTools.downCaseFirstLetter(typeStruture) + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>changed the return type";
		message += "<br>";
		return message;
	}
	
	protected String messageParameterTemplate(final String typeStruture, final String nameStrutureAfter, final String nameStrutureBefore, final String typePath, final String path){
		String message = "";
		message += "<br>" + typePath + " <code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += "<b>Category " + UtilTools.upperCaseFirstLetter(typeStruture) + " Parameters</b>:";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(nameStrutureBefore) + "</code>";
		message += "<br>changed the list parameters";
		message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(nameStrutureAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	protected String messageMoveTemplate(final String typeStruture,final String fullName, final String pathBefore, final String pathAfter){
		String message = "";
		message += UtilTools.downCaseFirstLetter(typeStruture) + "<code>" +StringEscapeUtils.escapeHtml4(fullName) + "</code>";
		message += "<br>moved from <code>" +StringEscapeUtils.escapeHtml4(pathBefore) + "</code>";
		message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(pathAfter) + "</code>";
		message += "<br>";
		return message;
	}

	protected String messageRenameTemplate(final String typeStruture, final String nameBefore, final String nameAfter, final String path){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(path) + "</code>: ";
		message += UtilTools.downCaseFirstLetter(typeStruture) + " <code>" +StringEscapeUtils.escapeHtml4(nameBefore) + "</code>";
		message += "<br>renamed to <code>" +StringEscapeUtils.escapeHtml4(nameAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String messagePullUpTemplate(final String typeStruture,  String nameStruture, final String nameClassBefore, final String nameClassAfter){
		String message = "";
		message += "<br> Pull Up " + UtilTools.upperCaseFirstLetter(typeStruture) +" <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>from <code>" +StringEscapeUtils.escapeHtml4(nameClassBefore) + "</code>";
		message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(nameClassAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String messagePushDownTemplate(final String typeStruture,  String nameStruture, final String nameClassBefore, final String nameClassAfter){
		String message = "";
		message += "<br> Push Down " + UtilTools.downCaseFirstLetter(typeStruture) +" <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>from <code>" +StringEscapeUtils.escapeHtml4(nameClassBefore) + "</code>";
		message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(nameClassAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String messageDeprecate(final String typeStruture, final String nameMethodBefore, final String nameClassBefore){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(nameClassBefore) + "</code>: ";
		message += "<br>deprecated ";
		message += "<br>" + typeStruture + " <code>" +StringEscapeUtils.escapeHtml4(nameMethodBefore) + "</code> ";
		message += "<br>";
		return message;
	}
	
	public String messageAddition(final String typeStruture, final String nameStruture, final String nameClass){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code>: ";
		message += "<br>added ";
		message += "<br>" + typeStruture + " <code>" +StringEscapeUtils.escapeHtml4(nameStruture) + "</code>";
		message += "<br>";
		return message;
	}
}
