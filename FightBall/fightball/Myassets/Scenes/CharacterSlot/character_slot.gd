extends Panel

var icon=null:
	set(value):
		icon=value
		$Texturebutton.texture_normal=value
		
signal pressed

func _ready():
	$hover.mouse_filter = Control.MOUSE_FILTER_IGNORE

func _button_pressed() -> void:
	for slot in get_parent().get_children():
		slot.deselect()
		slot.get_node("preessed").hide()
		
	$preessed.show()
	pressed.emit()	
		
func deselect():
	pass

func _on_mouse_entered() -> void:
	if $hover.visible==false:
		$hover.show() # Replace with function body.


func _on_mouse_exited() -> void:
	if $hover.visible==true:
		$hover.hide() # Rep# Replace with function body.
