extends Control


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/RegisterAndLogin/Login.tscn")



# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_pressed() -> void:
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)



func _on_button_register_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/RegisterAndLogin/register.tscn")


func _on_button_accept_pressed() -> void:
	pass # Replace with function body.
