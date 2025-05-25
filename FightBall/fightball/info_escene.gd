extends Node2D

@onready var tutorial_list = $TextureRect/ScrollContainer/TutorialList

func _ready() -> void:
	set_meta("scene_path", "res://infoEscene.tscn")
	_show_tutorials()

func _show_tutorials() -> void:
	# Limpiar tutoriales anteriores
	for child in tutorial_list.get_children():
		tutorial_list.remove_child(child)
		child.queue_free()

	# Crear tutoriales por defecto si está vacío
	if TutorialDataSource.DBtutorials.is_empty():
		print("ℹ️ No se encontraron tutoriales. Generando ejemplos por defecto.")
		var example_tutorials = [
			{ "title": "Cómo moverse", "description": "Usá las teclas WASD o flechas para moverte por el mapa." },
			{ "title": "Cómo atacar", "description": "Presioná clic izquierdo para atacar enemigos cercanos." },
			{ "title": "Cómo curarse", "description": "Usá pociones del inventario o descansá en una zona segura." }
		]

		for tut in example_tutorials:
			var tutorial = tutorialDB.new()
			tutorial.title = tut["title"]
			tutorial.description = tut["description"]
			TutorialDataSource.DBtutorials.append(tutorial)

	# Mostrar los tutoriales con textura de fondo
	for tut in TutorialDataSource.DBtutorials:
		var bg = NinePatchRect.new()
		bg.texture = load("res://Myassets/Img/fondoTutorial.png") # tu imagen de fondo para tutorial
		bg.custom_minimum_size = Vector2(0, 120)
		bg.patch_margin_left = 140
		bg.patch_margin_top = 20
		bg.patch_margin_right = 140
		bg.patch_margin_bottom = 20# ajustá esto según los bordes de tu imagen

		var content = VBoxContainer.new()
		content.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)

		var title_label = Label.new()
		title_label.text = "📘 " + tut.title
		title_label.add_theme_color_override("font_color", Color.SADDLE_BROWN)
		content.add_child(title_label)

		var desc_label = RichTextLabel.new()
		desc_label.text = tut.description
		desc_label.add_theme_color_override("font_color", Color.BLACK)
		desc_label.fit_content = true
		desc_label.scroll_active = false
		content.add_child(desc_label)

		bg.add_child(content)
		tutorial_list.add_child(bg)

		var separator = HSeparator.new()
		tutorial_list.add_child(separator)


func _on_go_back_pressed() -> void:
	SceneManager.go_back() # Replace with function body.
