extends Node2D

var test_Scene = preload("res://Myassets/testScenes/game.tscn")
var jCJ_Scene = preload("res://Myassets/Scenes/JCJScene/JCJScene.tscn")

# Llamado cuando el nodo entra en el árbol de la escena.
func _ready() -> void:
	pass 

# Llamado cada cuadro.
func _process(delta: float) -> void:
	pass

func _on_button_test_pressed() -> void:
	get_tree().change_scene_to_packed(test_Scene)

func _on_button_jcj_pressed() -> void:
	get_tree().change_scene_to_packed(jCJ_Scene)
