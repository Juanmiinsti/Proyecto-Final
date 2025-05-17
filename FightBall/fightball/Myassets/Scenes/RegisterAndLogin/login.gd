extends Control


# Called when the node enters the scene tree for the first time.

var http_request: HTTPRequest
signal session_ready  # <- ✅ señal nueva

func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/RegisterAndLogin/Login.tscn")
	http_request = HTTPRequest.new()
	add_child(http_request)
	http_request.request_completed.connect(_on_request_completed)


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_pressed() -> void:
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)



func _on_button_register_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/RegisterAndLogin/register.tscn")


func _on_button_accept_pressed() -> void:
	var username = $Username.text
	var password = $Password.text

	if username.is_empty() or password.is_empty():
		print("Por favor, completa todos los campos.")
		return

	var login_data = {
		"name": username,
		"password": password
	}
	
	var json_body = JSON.stringify(login_data)
	var url = PlayerInfo.urlSpring+"/auth/login" # Cambia esto por la IP real si hace falta

	var headers = ["Content-Type: application/json"]
	var result = http_request.request(url, headers, HTTPClient.METHOD_POST, json_body)

	if result != OK:
		print("Error al realizar la peticion ")
		
func _on_request_completed(result, response_code, headers, body):
	if response_code == 200:
		var token = body.get_string_from_utf8()

		PlayerInfo.userName = $Username.text
		PlayerInfo.userPassword = $Password.text
		PlayerInfo.userKey = token

		print("✅ Login exitoso, token recibido.")
		print("🔍 Buscando ID del usuario...")

		# Pedimos la info del usuario por nombre
		var user_info_request = HTTPRequest.new()
		add_child(user_info_request)
		user_info_request.timeout = 5

		user_info_request.request_completed.connect(
			func(_res, code, _headers, user_body):
				user_info_request.queue_free()
				
				if code == 200:
					var user_json = JSON.parse_string(user_body.get_string_from_utf8())
					PlayerInfo.userID = int(user_json["id"])
					print("✅ ID de usuario:", PlayerInfo.userID)

					PlayerInfo.save_user_data()  # Guardamos todo junto
					
					SceneManager.reset_history()
					SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)
				else:
					print("❌ Error al obtener datos del usuario:", code)
		)

		var url = PlayerInfo.urlSpring+"/api/userByName/%s" % PlayerInfo.userName
		var err = user_info_request.request(url, ["Accept: application/json"])

		if err != OK:
			print("❌ Error al pedir ID de usuario:", err)
	else:
		print("❌ Login fallido. Código:", response_code)

		
