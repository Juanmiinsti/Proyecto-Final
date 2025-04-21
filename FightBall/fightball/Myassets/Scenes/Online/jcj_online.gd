extends Control

@onready var lobby_list = $MarginContainer/LobbyListContainer
@onready var button_create = $MarginContainer/LobbyListContainer/HBoxContainer/ButtonCreate
@onready var button_back = $MarginContainer/LobbyListContainer/HBoxContainer/ButtonBack
@onready var status_label = $MarginContainer/LobbyListContainer/StatusLabel

func _ready():
	set_meta("scene_path", "res://Myassets/Scenes/JCJOnline/LobbyScene.tscn")

	status_label.text = "🔌 Conectando al servidor..."

	# Lógica temporal de prueba para mostrar partidas falsas
	await get_tree().create_timer(1.0).timeout
	populate_dummy_lobbies()

func populate_dummy_lobbies():
	 # Limpia anteriores si hay

	for i in range(3):
		var btn = Button.new()
		btn.text = "Sala #" + str(i + 1)
		btn.size_flags_horizontal = Control.SIZE_EXPAND_FILL
		btn.pressed.connect(func():
			print("🔗 Intentando unirse a Sala #", i + 1)
			status_label.text = "⏳ Uniéndose a Sala #" + str(i + 1) + "..."
		)
		lobby_list.add_child(btn)

	status_label.text = "✅ Conectado. Selecciona una partida o crea una nueva."

func _on_ButtonCreate_pressed():
	print("🎮 Creando nueva partida...")
	status_label.text = "🛠️ Creando sala y esperando oponente..."

	# Aquí luego conectás con WebSocket o backend para crear la sala

func _on_ButtonBack_pressed():
	SceneManager.go_back()
