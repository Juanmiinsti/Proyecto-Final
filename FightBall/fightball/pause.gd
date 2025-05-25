extends CanvasLayer



func _physics_process(delta):
	if Input.is_action_just_pressed("pause"):
		get_tree().paused=not get_tree().paused
		$TextureRect.visible=not $TextureRect.visible
		$TextureRect/ButtonExit.visible=not $TextureRect/ButtonExit.visible


func _on_button_exit_pressed() -> void:
	get_tree().paused = false
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn")
