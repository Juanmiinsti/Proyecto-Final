# SceneManager.gd
extends Node

var scene_stack: Array[String] = []

func go_to(scene_path: String, save_current := true) -> void:
	if save_current and get_tree().current_scene:
		var current = get_tree().current_scene
		if current.has_meta("scene_path"):
			scene_stack.append(current.get_meta("scene_path"))

	if ResourceLoader.exists(scene_path, "PackedScene"):
		get_tree().change_scene_to_file(scene_path)
	else:
		push_error("🚫 Escena no encontrada: " + scene_path)

func go_back() -> void:
	if scene_stack.size() > 0:
		var previous_path = scene_stack.pop_back()
		get_tree().change_scene_to_file(previous_path)
	else:
		push_warning("⚠ No hay escenas en el historial.")

func reset_history() -> void:
	scene_stack.clear()
	
@rpc("authority")  # Solo el servidor puede ejecutarla
func change_to_online_match():
	get_tree().change_scene_to_file("res://Myassets/Scenes/OnlineMatch/OnlineMatch.tscn")
