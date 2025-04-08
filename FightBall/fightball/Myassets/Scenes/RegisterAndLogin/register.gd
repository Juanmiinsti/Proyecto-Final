extends Control


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func checkPassword () -> bool:
	return $Password.text==$ConfirmPassword.text

func checkEdits()-> bool:
	if $Username.text=='':
		return false
	
	if $Password.text=='':
		return false
		
	if $ConfirmPassword.text=='':
		return false
	
	return true	

func _on_button_pressed() -> void:
	if checkEdits() and checkPassword():
		pass
