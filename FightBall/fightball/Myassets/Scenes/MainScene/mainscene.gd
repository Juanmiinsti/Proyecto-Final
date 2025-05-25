extends Node2D

func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/MainScene/Mainscene.tscn")
	PlayerInfo.load_user_data()  # 🔁 Carga los datos del usuario desde disco
	checkApiStatus()

func _on_button_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")

func _on_button_2_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/RegisterAndLogin/Login.tscn")

# Verifica que el backend esté activo
func checkApiStatus():
	var http = HTTPRequest.new()
	add_child(http)
	http.timeout = 5
	http.request_completed.connect(_on_ping_response)

	var url = PlayerInfo.urlSpring+"/api/prueba"
	var headers = ["Accept: application/json"]
	var error = http.request(url, headers)

	if error != OK:
		print("Error en petición:", error)
		$apiEstatus.text = "❌ Error al contactar API"
		http.queue_free()
		return

	$apiEstatus.text = "Conectando..."

func _on_ping_response(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	var http = HTTPRequest.new()
	if http:
		http.queue_free()

	match response_code:
		200:
			$apiEstatus.text = "✅ API Conectada"
			PlayerInfo.status=true
		404:
			$apiEstatus.text = "❌ Ruta no encontrada"
		_:
			$apiEstatus.text = "❌ Error HTTP %d" % response_code
		

func _on_button_log_out_pressed() -> void:
	print("🚪 Cerrando sesión...")

	# 1. Borrar datos de sesión
	PlayerInfo.clear_session()


func _on_info_pressed() -> void:
	SceneManager.go_to("res://infoEscene.tscn") # Replace with function body.
