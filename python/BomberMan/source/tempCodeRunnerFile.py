    time_str = str(int(get_passed_gametime()/1000)) + "s"
        text = bar_font.render(time_str, True, GREEN)
        print(time_str)
        screen.blit(text, (1 * TILE, (1 * TILE - bar_font.size(time_str)[1]) // 2))