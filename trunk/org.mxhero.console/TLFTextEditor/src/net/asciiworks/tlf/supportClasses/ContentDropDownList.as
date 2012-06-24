package net.asciiworks.tlf.supportClasses {
    import flash.display.DisplayObject;
    import flash.events.Event;
    import flash.events.MouseEvent;

    import spark.components.Button;
    import spark.components.DropDownList;
    import spark.components.TextInput;

    //--------------------------------------
    //  Events
    //--------------------------------------

    [Event(name="submit", type="net.asciiworks.tlf.supportClasses.ContentDropDownListEvent")]

    public class ContentDropDownList extends DropDownList {

        //--------------------------------------------------------------------------
        //
        //  Constructor
        //
        //--------------------------------------------------------------------------

        public function ContentDropDownList() {
            super();
        }

        //--------------------------------------------------------------------------
        //
        //  Skin parts
        //
        //--------------------------------------------------------------------------    

        [SkinPart(required="true")]
        public var payload:Object;

        [SkinPart(required="true")]
        public var submitButton:Button;

        //--------------------------------------------------------------------------
        //
        //  Variables
        //
        //--------------------------------------------------------------------------

        [Bindable]
        public var content:Object;

        //--------------------------------------------------------------------------
        //
        //  Methods
        //
        //--------------------------------------------------------------------------

        public function setHitAreaInclusions(exclusionObjects:Array):void {
            var objects:Vector.<DisplayObject> = new Vector.<DisplayObject>();
            for each (var obj:DisplayObject in exclusionObjects) {
                objects.push(obj);
            }
            dropDownController.hitAreaAdditions = objects;
        }

        //--------------------------------------------------------------------------
        //
        //  Overridden methods
        //
        //--------------------------------------------------------------------------

        override protected function partAdded(partName:String, instance:Object):void {
            super.partAdded(partName, instance);

            if (instance == submitButton) {
                submitButton.addEventListener(MouseEvent.CLICK, submitButton_clickHandler);
            }
        }

        override protected function partRemoved(partName:String, instance:Object):void {
            super.partRemoved(partName, instance);

            if (instance == submitButton) {
                submitButton.removeEventListener(MouseEvent.CLICK, submitButton_clickHandler);
            }
        }

        //--------------------------------------------------------------------------
        //
        //  Overridden methods
        //
        //--------------------------------------------------------------------------

        private function submitButton_clickHandler(event:MouseEvent):void {
			closeDropDown(false);
            var e:ContentDropDownListEvent = new ContentDropDownListEvent(ContentDropDownListEvent.SUBMIT, payload);
            dispatchEvent(e);
        }

    }
}