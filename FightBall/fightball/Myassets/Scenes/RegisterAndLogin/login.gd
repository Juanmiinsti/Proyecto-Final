extends Control


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	set_meta("packed_scene", preload("res://Myassets/Scenes/RegisterAndLogin/Login.tscn"))


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_pressed() -> void:
	SceneManager.go_back()



func _on_button_register_pressed() -> void:
	SceneManager.go_to(preload("res://Myassets/Scenes/RegisterAndLogin/register.tscn"))
