extends Node2D


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	set_meta("packed_scene", preload("res://Myassets/Scenes/CharacterSelection/Character_Selection.tscn"))
 # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_start_pressed() -> void:
	if not Persistence.character == null and not Persistence.character2 == null:
		SceneManager.go_to(preload("res://Myassets/Scenes/JCJScene/JCJScene.tscn"))
 
