# SceneManager.gd

extends Node

var scene_stack: Array[PackedScene] = []

# Cambiar a una nueva escena y guardar la actual
func go_to(packed_scene: PackedScene, save_current: bool = true) -> void:
	if save_current and get_tree().current_scene:
		var current = get_tree().current_scene
		if current.has_meta("packed_scene"):
			scene_stack.append(current.get_meta("packed_scene"))
		else:
			push_warning("La escena actual no tiene meta 'packed_scene' asignado. No se puede guardar en el stack.")
	
	get_tree().change_scene_to_packed(packed_scene)

# Volver a la escena anterior si existe
func go_back() -> void:
	if scene_stack.size() > 0:
		var previous = scene_stack.pop_back()
		get_tree().change_scene_to_packed(previous)
	else:
		print("⚠️ No hay escena anterior en el stack.")

# Limpiar historial (por ejemplo, cuando volvés al MainScene)
func reset_history() -> void:
	scene_stack.clear()
