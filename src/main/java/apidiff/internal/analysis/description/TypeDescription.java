package apidiff.internal.analysis.description;

import apidiff.internal.util.UtilTools;
import org.apache.commons.text.StringEscapeUtils;

public class TypeDescription extends TemplateDescription {

	public String rename(final String fullNameBefore, final String fullNameAfter){
		String message = "";
		message += "type <code>" + StringEscapeUtils.escapeHtml4(fullNameBefore) + "</code>";
		message += "<br>renamed to";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(fullNameAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String move(final String fullNameBefore, final String fullNameAfter){
		String message = "";
		message += "<br>type <code>" +StringEscapeUtils.escapeHtml4(fullNameBefore) + "</code>";
		message += "<br>moved to";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(fullNameAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String moveAndRename(final String fullNameBefore, final String fullNameAfter){
		String message = "";
		message += "<br>type <code>" +StringEscapeUtils.escapeHtml4(fullNameBefore) + "</code>";
		message += "<br>moved and renamed to";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(fullNameAfter) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String remove(final String nameClass){
		String message = "";
		message += "<br>type <code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code> ";
		message += "<br>was removed";
		message += "<br>";
		return message;
	}
	
	public String visibility(final String nameClass, final String visibility1, final String visibility2){
		String message = "";
		message += "<br> type <code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code>";
		message += "<br> changed visibility from <code>" +StringEscapeUtils.escapeHtml4(visibility1 ) + "</code> to <code>"  + StringEscapeUtils.escapeHtml4(visibility2) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String modifierStatic(final String nameClass, final Boolean isGain){
		String message = "";
		message += "type <code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code>";
		message += isGain ? "<br>received the modifier <code>static</code>" : "<br>lost the modifier <code>static</code>";
		message += "<br>";
		return message;
	}
	
	public String modifierFinal(final String nameClass, final Boolean isGain){
		String message = "";
		message += "type <code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code>";
		message += isGain ? "<br>received the modifier <code>final</code>" : "<br>lost the modifier <code>final</code>";
		message += "<br>";
		return message;
	}
	
	public String changeSuperClass(final String nameClassComplete, final String listSuperClass1, final String listSuperClass2){
		String message = "";
		message += "<b>Category Superclass</b>:";
		message += "<br>class <code>" +StringEscapeUtils.escapeHtml4(nameClassComplete) + "</code>";
		message += "<br>changed the superclass list";
		message += "<br>from <code>" +StringEscapeUtils.escapeHtml4(listSuperClass1) + "</code>";
		message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(listSuperClass2) + "</code>";
		message += "<br>";
		return message;
	}
	
	public String changeSuperType(final String nameClassComplete, final String superTypeBefore,  final String superTypeAfter){
		String message = "";
		message += "<br><code>" +StringEscapeUtils.escapeHtml4(nameClassComplete) + "</code>";
		
		
		if(!UtilTools.isNullOrEmpty(superTypeBefore) && !UtilTools.isNullOrEmpty(superTypeAfter)){
			message += "<br>changed the superClass";
			message += "<br>from <code>" +StringEscapeUtils.escapeHtml4(superTypeBefore) + "</code>";
			message += "<br>to <code>" +StringEscapeUtils.escapeHtml4(superTypeAfter) + "</code>";
		}
		
		if(UtilTools.isNullOrEmpty(superTypeBefore) && !UtilTools.isNullOrEmpty(superTypeAfter)){
			message += "<br>added superClass " + StringEscapeUtils.escapeHtml4(superTypeAfter) + "</code>";
		}
		
		if(!UtilTools.isNullOrEmpty(superTypeBefore) && UtilTools.isNullOrEmpty(superTypeAfter)){
			message += "<br>removed superClass " + StringEscapeUtils.escapeHtml4(superTypeBefore) + "</code>";
		}

		message += "<br>";
		return message;
	}
	
	public String addition(final String nameClass){
		String message = "";
		message += "<br>Type <code>" +StringEscapeUtils.escapeHtml4(nameClass) + "</code> added";
		message += "<br>";
		return message;
	}
	
	public String deprecate(final String nameClass){
		String message = "";
		message += "<br> type " + nameClass + " <code> was deprecated";
		message += "<br>";
		return message;
	}
	
}
