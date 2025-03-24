extends Node

var getAllCharacter=HTTPRequest.new()

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
func _on_request_completed(result, response_code, headers, body):
	var json_string = body.get_string_from_utf8()
	var parsed_json = JSON.parse_string(json_string)

	if parsed_json is Array and parsed_json.size() > 0:
		var nombre = parsed_json[0]["name"]  # Obtener el nombre del JSON

		# Intentamos encontrar el Label
		var label = get_node("../Label")  # Ajusta la ruta si es necesario
		print("Label encontrado:", label)  # Esto nos dirá si se encuentra
		if label:
			label.text = nombre  # Asignamos el texto solo si el nodo existe
		else:
			print("Error: No se encontró el Label en la escena.")

	else:
		print("Error: La respuesta no es un array válido o está vacía")
