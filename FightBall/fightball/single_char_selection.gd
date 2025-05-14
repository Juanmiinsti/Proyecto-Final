extends Node2D


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	set_meta("scene_path", "res://singleCharSelection.tscn") # Replace with function body.
 # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_back_pressed() -> void:
	SceneManager.go_back()
 # Replace with function body.


func _on_start_pressed() -> void:
	if not Persistence.character == null:
		SceneManager.go_to("res://waveMode.tscn") 
