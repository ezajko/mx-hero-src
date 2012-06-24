package net.asciiworks.tlf.supportClasses {
    import flash.events.Event;

    public class ContentDropDownListEvent extends Event {
		
		public static const SUBMIT:String = "submit";
		
		public var payload:Object;
		
        public function ContentDropDownListEvent(type:String, payload:Object, bubbles:Boolean = false, cancelable:Boolean = false) {
            super(type, bubbles, cancelable);
			this.payload = payload;
        }
		
		override public function clone():Event {
			return new ContentDropDownListEvent(type,payload,bubbles,cancelable);
		}
    }
}