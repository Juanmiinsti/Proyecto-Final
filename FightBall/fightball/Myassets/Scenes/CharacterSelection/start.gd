extends Button

var go_to_JCJ=preload("res://Myassets/Scenes/JCJScene/JCJScene.tscn")
# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
	

	


func _on_start_pressed() -> void:
	if not Persistence.character == null and not Persistence.character2 == null:
		get_tree().change_scene_to_packed(go_to_JCJ)
	# Replace with function body.
