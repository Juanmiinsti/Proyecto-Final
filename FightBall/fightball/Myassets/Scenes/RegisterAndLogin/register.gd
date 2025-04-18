extends Control


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func checkPassword () -> bool:
	return $Password.text==$ConfirmPassword.text

func checkEdits()-> bool:
	if $Username.text=='':
		return false
	
	if $Password.text=='':
		return false
		
	if $ConfirmPassword.text=='':
		return false
	
	return true	

func _on_button_pressed() -> void:
	if checkEdits() and checkPassword():
		var username = $Username.text.strip_edges()
		var password = $Password.text.strip_edges()
		var ErrorMessage
		var http_check = HTTPRequest.new()
		add_child(http_check)
		http_check.timeout = 5
		
		var check_url = "http://localhost:8080/api/users/by-name?name=%s" % username
		
		http_check.request_completed.connect(
			func(_result, response_code, _headers, _body):
				http_check.queue_free()
				
				match response_code:
					200:
						ErrorMessage = "El usuario ya existe"
					404:
						var http_create = HTTPRequest.new()
						add_child(http_create)
						http_create.timeout = 5
						
						var create_url = "http://localhost:8080/api/users"
						var headers = ["Content-Type: application/json"]
						var body = JSON.stringify({
							"name": username,
							"password": password
						})
						
						http_create.request_completed.connect(
							func(_result, create_code, _headers, body):
								http_create.queue_free()
								
								if create_code == 201:
									$SuccessMessage.text = "¡Registro exitoso!"
									$Username.text = ""
									$Password.text = ""
									# Opcional: Cambiar de escena aquí
								else:
									var response = body.get_string_from_utf8()
									ErrorMessage = "Error al crear usuario (Código %d)" % create_code
						)
						
						var error = http_create.request(create_url, headers, HTTPClient.METHOD_POST, body)
						if error != OK:
							http_create.queue_free()
							ErrorMessage = "Error en la conexión"
					_:
						ErrorMessage = "Error al verificar usuario (Código %d)" % response_code
		)
		
		var error = http_check.request(check_url)
		if error != OK:
			http_check.queue_free()
			ErrorMessage.text = "Error al iniciar verificación"
	
