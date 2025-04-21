extends Node2D





# Llamado cuando el nodo entra en el árbol de la escena.
func _ready() -> void:
	set_meta("packed_scene", preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn"))
 

# Llamado cada cuadro.
func _process(delta: float) -> void:
	pass

func _on_button_test_pressed() -> void:
	pass

func _on_button_jcj_pressed() -> void:
	SceneManager.go_to(preload("res://Myassets/Scenes/CharacterSelection/Character_Selection.tscn"))
