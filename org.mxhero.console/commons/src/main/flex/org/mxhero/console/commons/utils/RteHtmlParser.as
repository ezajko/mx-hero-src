package org.mxhero.console.commons.utils
{
	import flash.xml.XMLDocument;
	import flash.xml.XMLNode;
	
	public class RteHtmlParser
	{
		public var SET_P:String = 'DIV';
		public var SET_LI:String = 'LI';
		public var SET_FONT:String = 'SPAN';
		
		public var SET_UL:String = 'UL';
		public var SET_BR:String = 'BR';
		
		public var ignoreParagraphSpace:Boolean = false;
		
		private var out_xml:XML;
		
		public function RteHtmlParser()
		{
		}
		
		// not those line breaks that kills the SQL
		public function get StringFormat():String
		{
			if (!out_xml) return '';
			
			XML.prettyIndent = 0;
			// remove the /n from the string
			// we want a string out!
			var s:String = unescape(unescape(escape(out_xml.children()).split('%0A').join('')));
			XML.prettyIndent = 2;
			
			return s;
		}
		
		// nice looking string
		public function get XMLFormat():String
		{
			if (!out_xml) return '';
			return unescape(out_xml.children().toXMLString());
		}
		
		// the xml
		public function get XMLObject():XML
		{
			return out_xml;
		}
		
		
		//________________________________________________________________________________________________________
		//                                                                                             HTML PARSER
		
		public function ParseToHTML(string:String):void
		{
			var xml_doc:XMLDocument = new XMLDocument("<BODY>"+string+"</BODY>");
			var nxml:XMLNode = (ignoreParagraphSpace) ? xml_doc.firstChild : manage_space(xml_doc.firstChild);
			
			var xml:XML = XML(nxml.toString());
			var t1:XML;
			
			// Remove all TEXTFORMAT
			for( t1 = xml..TEXTFORMAT[0]; t1 != null; t1 = xml..TEXTFORMAT[0] ) {
				t1.parent().replace( t1.childIndex(), t1.children() );
			}
			
			//add br tag
			if (SET_BR)
				xml = add_br_tag(xml);
			
			// add ul tag
			xml = add_ul_tag(xml);
			
			// format css
			xml = add_css(xml);
			
			// format new names
			xml = set_new_name(xml);
			
			out_xml = xml;
		}
		
		private function add_ul_tag(xml:XML):XML
		{
			var t1:XML;
			var t2:XML = new XML(<BODY />);
			var el:XMLList = xml.children();
			var ul:XML;
			
			for each (t1 in el) {
				if (t1.name().localName != 'LI') {
					t2.appendChild(t1.copy());
					
				} else if (t1.childIndex() == 0) {
					ul = new XML(<UL />);
					ul.appendChild(t1.copy());
					t2.appendChild(ul);
					
				} else if (el[t1.childIndex()-1].name().localName != 'LI') {
					ul = new XML(<UL />);
					ul.appendChild(t1.copy());
					t2.appendChild(ul);
					
				} else {
					ul.appendChild(t1.copy());
					
				}
			}
			
			return t2;
		}
		
		private function add_br_tag(xml:XML):XML
		{
			var br:XML;
			
			for each (var i:XML in xml.children()) {
				if (!has_text(i)) {
					br = copy_attributes(i.descendants('FONT')[0], new XML(<BR />));
					i.parent().replace(i.childIndex(), br);
				}
			}
			return xml;
		}
		
		private function set_new_name(xml:XML):XML
		{
			var t1:XML;
			// set new P
			if (SET_P == null)
			{
				for( t1 = xml..P[0]; t1 != null; t1 = xml..P[0] ) {
					t1.parent().replace(t1.childIndex(), t1.children());
				}
			}
			else if (SET_P.toLocaleUpperCase() != 'P') {
				for( t1 = xml..P[0]; t1 != null; t1 = xml..P[0] ) {
					t1.setName(SET_P);
				}
			}
			// set new UL
			if (SET_UL == null)
			{
				for( t1 = xml..UL[0]; t1 != null; t1 = xml..UL[0] ) {
					t1.parent().replace(t1.childIndex(), t1.children());
				}
			}
			else if (SET_UL.toLocaleUpperCase() != 'UL') {
				for( t1 = xml..UL[0]; t1 != null; t1 = xml..UL[0] ) {
					t1.setName(SET_FONT);
				}
			}
			// set new LI
			if (SET_LI == null)
			{
				for( t1 = xml..LI[0]; t1 != null; t1 = xml..LI[0] ) {
					t1.parent().replace(t1.childIndex(), t1.children());
				}
			}
			else if (SET_LI.toLocaleUpperCase() != 'LI') {
				for( t1 = xml..LI[0]; t1 != null; t1 = xml..LI[0] ) {
					t1.setName(SET_LI);
				}
			}
			// set new FONT
			if (SET_FONT == null)
			{
				for( t1 = xml..FONT[0]; t1 != null; t1 = xml..FONT[0] ) {
					t1.parent().replace(t1.childIndex(), t1.children());
				}
			}
			else if (SET_FONT.toLocaleUpperCase() != 'FONT') {
				for( t1 = xml..FONT[0]; t1 != null; t1 = xml..FONT[0] ) {
					t1.setName(SET_FONT);
					// wierd browser rendering on e.g. <span />
					if (t1 == '') {
						//t1.setChildren('');
						//or you can just replace it with nothing
						t1.parent().replace(t1.childIndex(), '');
					}
				}
			}
			// set new BR
			if (SET_BR == null)
			{
				for( t1 = xml..BR[0]; t1 != null; t1 = xml..BR[0] ) {
					t1.parent().replace(t1.childIndex(), t1.children());
				}
			}
			else if (SET_BR.toLocaleUpperCase() != 'BR') {
				for( t1 = xml..BR[0]; t1 != null; t1 = xml..BR[0] ) {
					t1.setName(SET_BR);
					// if it's closed like this <font /> in html
					// thebrowser might act wierd!
					t1.setChildren('');
				}
			}
			return xml;
		}
		
		private function add_css(xml:XML):XML
		{
			var t1:XML;
			var t2:XML;
			// Find all ALIGN
			for each ( t1 in xml..@ALIGN ) {
				t2 = t1.parent();
				t2.@STYLE = "text-align:" + t1 + ";" + t2.@STYLE;
				delete t2.@ALIGN;
			}
			// Find all FACE
			for each ( t1 in xml..@FACE ) {
				t2 = t1.parent();
				t2.@STYLE = "font-family:'" + t1 + "';" + t2.@STYLE;
				delete t2.@FACE;
			}
			// Find all SIZE 
			for each ( t1 in xml..@SIZE ) {
				t2 = t1.parent();
				t2.@STYLE = "font-size:" + t1 + "px;" + t2.@STYLE;
				delete t2.@SIZE;
			}
			// Find all COLOR 
			for each ( t1 in xml..@COLOR ) {
				t2 = t1.parent();
				t2.@STYLE = "color:" + t1 + ";" + t2.@STYLE;
				delete t2.@COLOR;
			}
			// Find all LETTERSPACING 
			for each ( t1 in xml..@LETTERSPACING ) {
				t2 = t1.parent();
				t2.@STYLE = "letter-spacing:" + t1 + "px;" + t2.@STYLE;
				delete t2.@LETTERSPACING;
			}
			// Find all KERNING
			for each ( t1 in xml..@KERNING ) {
				t2 = t1.parent();
				// ? css 
				delete t2.@KERNING;
			}
			return xml;
		}
		
		//________________________________________________________________________________________________________
		
		
		//________________________________________________________________________________________________________
		//                                                                                              RTE PARSER
		
		public function ParseToRTE(string:String):void
		{
			var xml_doc:XMLDocument = new XMLDocument("<BODY>"+string+"</BODY>");
			var nxml:XMLNode = (ignoreParagraphSpace) ? xml_doc.firstChild : manage_space(xml_doc.firstChild);
			
			var xml:XML = new XML(nxml.toString());
			
			// remove UL
			xml = remove_ul_tag(xml);
			
			// remove BR
			xml = remove_br_tag(xml);
			
			// format CSS
			xml = remove_css(xml);
			
			// format names
			xml = rename_tags(xml);
			
			//add TEXTFORMAT
			xml = add_textformat(xml);
			
			out_xml = xml;
		}
		
		private function remove_ul_tag(xml:XML):XML
		{
			var ul:XMLList = xml.elements(SET_UL);
			
			for each (var i:XML in ul) {
				i.parent().replace(i.childIndex(), i.children());
			}
			
			return xml;
		}
		
		private function remove_br_tag(xml:XML):XML
		{
			var br:XMLList = xml.descendants(SET_BR);
			var p:XML;
			var f:XML;
			
			for each (var i:XML in br) {
				p = new XML(<P />);
				f = copy_attributes(i, new XML(<FONT />));
				f.setChildren('');
				p.appendChild(f);
				i.parent().replace(i.childIndex(), p);
			}
			
			return xml;
		}
		
		private function remove_css(xml:XML):XML
		{
			var ar:Array;
			var ta:Array;
			var el:XML;
			var name:String;
			
			for each ( var i:XML in xml..@STYLE ) {
				el = i.parent();
				ar = String(el.@STYLE).split(';');
				
				for (var j:uint = 0; j < ar.length; j++) {
					ta = ar[j].split(':');
					name = ta[0].toLocaleLowerCase().split(' ').join('');
					
					switch (name) {
						case 'text-align':
							el.@ALIGN = ta[1];
							break;
						
						case 'font-family':
							el.@FACE = ta[1].split("'").join('').split('"').join('');
							break;
						
						case 'font-size':
							el.@SIZE = ta[1].split('px').join('');
							break;
						
						case 'color':
							el.@COLOR = ta[1];
							break;
						
						case 'letter-spacing':
							el.@LETTERSPACING = ta[1].split('px').join('');
							break;
					}
				}
				
				delete el.@STYLE;
			}
			
			return xml;
		}
		
		private function rename_tags(xml:XML):XML
		{
			var t:XML;
			var el:XMLList;
			
			// set new P
			if (SET_P.toLocaleUpperCase() != 'P' && SET_P != null) {
				el = xml.descendants(SET_P);
				for each (t in el) {
					t.setName('P');
				}
			}
			// set new LI
			if (SET_LI.toLocaleUpperCase() != 'LI' && SET_LI != null) {
				el = xml.descendants(SET_LI);
				for each (t in el) {
					t.setName('LI');
				}
			}
			// set new FONT
			if (SET_FONT.toLocaleUpperCase() != 'FONT' && SET_FONT != null) {
				el = xml.descendants(SET_FONT);
				for each (t in el) {
					t.setName('FONT');
				}
			}
			return xml;
		}
		
		private function add_textformat(xml:XML):XML
		{
			var m:XML = new XML(<BODY />);
			var tf:XML;
			
			for each (var i:XML in xml.children()) {
				tf = new XML(<TEXTFORMAT />);
				//tf.@LEADING = '2';
				tf.appendChild(i.copy());
				m.appendChild(tf);
			}
			
			return m;
		}
		
		//________________________________________________________________________________________________________
		
		
		
		//________________________________________________________________________________________________________
		//                                                                                              SOME TOOLS
		
		private function has_text(xml:XML):Boolean
		{
			for each (var i:XML in xml.children()) {
				if (i.nodeKind() == 'text') return true;
				else if (has_text(i)) return true;
			}
			
			return false;
		}
		
		private function copy_attributes(x1:XML, x2:XML):XML
		{
			for each (var i:XML in x1.attributes()) {
				x2.@[i.name().localName] = i;
			}
			return x2;
		}
		
		private function manage_space(node:XMLNode):XMLNode
		{
			for each (var n:XMLNode in node.childNodes)
			{
				if (n.nodeType == 3) n.nodeValue = n.nodeValue.split(' ').join('%20');
				if (n.hasChildNodes()) manage_space(n);
			}
			return node;
		}
		
		//________________________________________________________________________________________________________
	}
}