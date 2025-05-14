extends Node2D




@onready var button_online = $button_online # Asegurate de que esta sea la ruta correcta

# Llamado cuando el nodo entra en el árbol de la escena.
func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
# ✅ Habilita o desactiva el botón según si el usuario está logueado
	#if PlayerInfo.userID > 0 and PlayerInfo.status:
	#	button_online.disabled = false
	#else:
	#	button_online.disabled = true
	#	print("⚠️ Usuario no logueado, botón JCJ Online deshabilitado")
 

# Llamado cada cuadro.
func _process(delta: float) -> void:
	pass

func _on_button_test_pressed() -> void:
	pass

func _on_button_jcj_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/CharacterSelection/Character_Selection.tscn")


func _on_button_back_pressed() -> void:
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)


func _on_button_online_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/Online/JcjOnline.tscn")


func _on_button_jcia_pressed() -> void:
	SceneManager.go_to("res://singleCharSelection.tscn")
