extends CanvasLayer

func _physics_process(delta):
	if Input.is_action_just_pressed("pause"):
		get_tree().paused=not get_tree().paused
		$ColorRect.visible=not $ColorRect.visible

func _on_button_exit_pressed() -> void:
	get_tree().paused = false
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn")
