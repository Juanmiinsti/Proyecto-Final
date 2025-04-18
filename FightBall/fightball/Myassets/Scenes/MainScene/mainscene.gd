extends Node2D

var go_to_selectModeScene=preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
var go_to_loginScene=preload("res://Myassets/Scenes/RegisterAndLogin/Login.tscn")
# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	checkApiStatus()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_pressed() -> void:
	get_tree().change_scene_to_packed(go_to_selectModeScene)# Replace with function body.


func _on_button_2_pressed() -> void:
	get_tree().change_scene_to_packed(go_to_loginScene)


func checkApiStatus():
	var http = HTTPRequest.new()
	add_child(http)
	
	# Configurar timeout (opcional pero recomendado)
	http.timeout = 5
	
	# Conectar señal usando sintaxis moderna de Godot 4
	http.request_completed.connect(_on_ping_response)
	
	# URL - Cambia a http si es local sin SSL
	var url = "http://localhost:8080/api/prueba"  
	
	# Headers básicos
	var headers = ["Accept: application/json"]
	
	# Realizar petición
	var error = http.request(url, headers)
	
	if error != OK:
		print("Error en la petición: ", error)
		$apiEstatus.text = "❌ Error en petición"
		http.queue_free()
		return
	
	$apiEstatus.text = "Conectando..."
	print("Petición API iniciada")

func _on_ping_response(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	var http = get_node("HTTPRequest")
	if http:
		http.queue_free()
	
	# Debug: mostrar respuesta cruda
	var response = body.get_string_from_utf8()
	print("Código: ", response_code, " | Respuesta: ", response)
	
	match response_code:
		200:
			$apiEstatus.text = "✅ API Conectada"
			print("Conexión exitosa")
		404:
			$apiEstatus.text = "❌ Ruta no encontrada"
		_:
			$apiEstatus.text = "❌ Error HTTP %d" % response_code
